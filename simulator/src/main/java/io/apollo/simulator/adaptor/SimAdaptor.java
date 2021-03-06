package io.apollo.simulator.adaptor;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import io.apollo.simulator.persistence.avro.entity.MarketDataLevel1;
import io.apollo.simulator.persistence.avro.entity.MarketDataSubscribe;
import io.apollo.simulator.persistence.avro.entity.SimOrder;
import io.gemini.definition.account.Account;
import io.gemini.definition.adaptor.AdaptorBaseImpl;
import io.gemini.definition.adaptor.Command;
import io.gemini.definition.market.data.impl.BasicMarketData;
import io.gemini.definition.market.instrument.Instrument;
import io.gemini.definition.order.ActualChildOrder;
import io.gemini.definition.order.Order;
import io.gemini.definition.order.OrderKeeper;
import io.gemini.definition.order.enums.OrdStatus;
import io.gemini.definition.order.structure.OrdReport;
import io.gemini.definition.strategy.StrategyScheduler;
import io.mercury.common.param.ImmutableParams;
import io.mercury.serialization.avro.AvroBinaryDeserializer;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.core.api.Sender;
import io.mercury.transport.socket.SocketReceiver;
import io.mercury.transport.socket.SocketSender;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public class SimAdaptor extends AdaptorBaseImpl<BasicMarketData> {

	private Receiver mdReceiver;
	private Receiver tdReceiver;

	private Sender<byte[]> mdSender;
	private Sender<byte[]> tdSender;

	private StrategyScheduler<BasicMarketData> scheduler;

	protected ImmutableParams<SimAdaptorParamKey> paramMap;

	protected SocketConfigurator mdConfigurator;

	protected SocketConfigurator tdConfigurator;

	private Function<MarketDataLevel1, BasicMarketData> marketDataFunction = marketDataLevel1 -> {
		return null;
	};

	private Function<SimOrder, OrdReport> orderFunction = order -> {
		return null;
	};

	private AvroBinaryDeserializer<MarketDataLevel1> marketDataDeserializer = new AvroBinaryDeserializer<>(
			MarketDataLevel1.class);

	private AvroBinaryDeserializer<SimOrder> orderDeserializer = new AvroBinaryDeserializer<>(SimOrder.class);

	public SimAdaptor(int adaptorId, @Nonnull Account account, @Nonnull ImmutableParams<SimAdaptorParamKey> paramMap,
			StrategyScheduler<BasicMarketData> scheduler) {
		super(adaptorId, "SimulatorAdaptor[" + adaptorId + "]", scheduler, account);
		this.paramMap = paramMap;
		this.scheduler = scheduler;
		SocketConfigurator mdConfigurator = SocketConfigurator.builder()
				.host(paramMap.getString(SimAdaptorParamKey.MdHost)).port(paramMap.getInt(SimAdaptorParamKey.MdPort))
				.build();
		SocketConfigurator tdConfigurator = SocketConfigurator.builder()
				.host(paramMap.getString(SimAdaptorParamKey.TdHost)).port(paramMap.getInt(SimAdaptorParamKey.TdPort))
				.build();
		this.mdReceiver = new SocketReceiver(mdConfigurator, (bytes) -> {
			List<MarketDataLevel1> marketDatas = marketDataDeserializer.deserializationMultiple(bytes);
			for (MarketDataLevel1 marketData : marketDatas) {
				this.scheduler.onMarketData(marketDataFunction.apply(marketData));
			}
		});
		this.tdReceiver = new SocketReceiver(tdConfigurator, (bytes) -> {
			List<SimOrder> orders = orderDeserializer.deserializationMultiple(bytes);
			for (SimOrder order : orders) {
				this.scheduler.onOrdReport(orderFunction.apply(order));
			}
		});

		this.mdSender = new SocketSender(mdConfigurator);
		this.tdSender = new SocketSender(tdConfigurator);
	}

	@Override
	public boolean startup0() {
		mdReceiver.receive();
		tdReceiver.receive();
		return mdReceiver.isConnected() && tdReceiver.isConnected();
	}

	@Override
	public String adaptorName() {
		return "SimInboundAdaptor$" + this.hashCode();
	}

	@Override
	public boolean subscribeMarketData(Instrument... instruments) {

		MarketDataSubscribe simSubscribe = MarketDataSubscribe.newBuilder().setUniqueId(Integer.valueOf(1))
				.setStartTradingDay(paramMap.getString(SimAdaptorParamKey.TradingDayStart))
				.setEndTradingDay(paramMap.getString(SimAdaptorParamKey.TradingDayEnd))
				.setInstrumentIdList(
						Stream.of(instruments).map(instrument -> instrument.code()).collect(Collectors.toList()))
				.build();
		byte[] byteMsg;
		try {
			byteMsg = simSubscribe.toByteBuffer().array();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		mdSender.send(byteMsg);
		return true;
	}

	@Override
	public boolean newOredr(Account account, ActualChildOrder order) {
		SimOrder simOrder = SimOrder.newBuilder().setOrderRef(Long.valueOf(order.uniqueId()).intValue())
				.setInstrumentId(order.instrument().code()).setLimitPrice(order.price().offerPrice())
				.setVolumeTotalOriginal(Double.valueOf(order.qty().offerQty()).intValue())
				.setOrderStatus(OrdStatus.PendingNew.code()).setDirection(order.direction().code()).build();
		byte[] byteMsg;
		try {
			byteMsg = simOrder.toByteBuffer().array();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tdSender.send(byteMsg);
		return true;
	}

	@Override
	public boolean cancelOrder(Account account, ActualChildOrder order) {
		Order cancelOrder = OrderKeeper.getOrder(order.uniqueId());
		SimOrder simOrder = SimOrder.newBuilder().setOrderRef(Long.valueOf(order.uniqueId()).intValue())
				.setInstrumentId(cancelOrder.instrument().code()).setLimitPrice(order.price().offerPrice())
				.setVolumeTotalOriginal(Double.valueOf(order.qty().offerQty()).intValue())
				.setOrderStatus(OrdStatus.PendingCancel.code()).setDirection(cancelOrder.direction().code()).build();
		byte[] byteMsg;
		try {
			byteMsg = simOrder.toByteBuffer().array();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tdSender.send(byteMsg);
		return true;
	}

	@Override
	public boolean queryOrder(Account account, Instrument instrument) {
		return false;
	}

	@Override
	public boolean queryPositions(Account account, Instrument instrument) {
		return false;
	}

	@Override
	public boolean queryBalance(Account account) {
		return false;
	}

	@Override
	public void close() throws IOException {
		mdSender.destroy();
		tdSender.destroy();
		mdReceiver.destroy();
		tdReceiver.destroy();
	}

	@Override
	public boolean sendCommand(Command command) {
		return false;
	}

}
