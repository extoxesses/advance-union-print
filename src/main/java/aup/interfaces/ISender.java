package aup.interfaces;

public interface ISender<T extends IMessage> {

	public void send(T message);

}
