package application;

public class LinkedList<E> {
	
	private ListNode<E> front;
	
	public LinkedList(ListNode<E> node) {
		front = node;
	}
	 
	/**
	 * Adds a given node to the list. Either adds the node to an empty
	 * list, adds the node before the front, or calls a method to add
	 * the node in the middle.
	 * 
	 * @param newNode - Node to be added to the list
	 */
	public void add(E e) {
		ListNode<E> newNode = new ListNode<E>(e, null);
		if (front == null) {
			front = newNode;
		} else {
			ListNode<E> current = front;
			while (!(current.getNext() == null)) {
				current = current.getNext();
			}
			current.setNext(newNode);
		}
	}
	
	/**
	 * Deletes a given node from the list. First checks if the first node should be
	 * deleted. Then traverses the list, and either deletes the node or prints an
	 * error message if the node is not in the list.
	 * 
	 * @param node - Node to be deleted from the list
	 */
	public void delete(ListNode<E> node) {
		if (front != null && node.getData().equals(front.getData())) {
			front = front.getNext();
		} else {
			ListNode<E> current = front, prior = front;
			while (current != null && !(node.getData().equals(current.getData()))) {
				prior = current;
				current = current.getNext();
			}
			if (current == null) {
				System.out.println("Not in List.");
			} else {
				prior.setNext(current.getNext());
			}
		}
	}
	
	public ListNode<E> getFront() {
		return front;
	}
	
}
