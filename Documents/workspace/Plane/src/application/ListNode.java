package application;

public class ListNode<E> {
	
	private E data;
	private ListNode<E> next;
	
	
	public ListNode(E newData, ListNode<E> newNext) {
		data = newData;
		next = newNext;
	}
	
	public E getData() {
		return data;
	}
	
	public ListNode<E> getNext() {
		return next;
	}
	
	public void setData(E newData) {
		data = newData;
	}
	
	public void setNext(ListNode<E> newNext) {
		next = newNext;
	}
	
}
