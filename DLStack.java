/**
 *Summary for DLStack class:
 *
 * In the PathFinder class, I implemented a Double Linked Stack (DLStack) to facilitate the pathfinding process.
 * The DLStack is a generic class that can store elements of any type and supports common stack operations like push,
 * pop, peek, isEmpty, and size.
 *
 * The push method adds a new element to the top of the stack, utilizing the DoubleLinkedNode class to maintain the
 * connections between nodes. The pop method removes and returns the top element from the stack, handling exceptions for
 * an empty stack. The pop(int k) method allows removing the k-th element from the top of the stack, also considering
 * invalid input scenarios. The peek method returns the top element without removing it from the stack, throwing an
 * exception if the stack is empty. The isEmpty method checks if the stack is empty by verifying the top node.
 * The size method returns the number of items in the stack.The toString method creates a formatted string representation
 * of the stack's elements.

 * Throughout the implementation, I aimed for precision and clarity, ensuring logical flow and adherence to the code's
 * functionality. I encountered challenges, particularly when dealing with edge cases and maintaining the integrity of
 * the stack. To test the solution, I designed comprehensive test cases to verify the correctness of each method's
 * behavior, including corner cases and boundary conditions. The PathFinder class serves as a crucial component in the
 * pathfinding algorithm, and its proper functioning is essential for the overall success of the program.
 *
 * @param <T> The <T> in the class definition and method signatures of the DLStack<T> class indicates the use of a type
 * parameter or a generic type. The letter "T" is a common convention for representing a generic type, but any valid Java
 * identifier can be used. Using a generic type allows the DLStack class to be instantiated with different data types.
 * Instead of creating separate classes for different data types (e.g., DLStack<Integer>, DLStack<String>), a single
 * generic class can handle multiple types.
 */

public class DLStack<T> implements DLStackADT<T>{

    /**
     * Stores the top of the stack.
     */
    private DoubleLinkedNode<T> top;

    /**
     * Stores the number of items in the stack.
     */
    private int numItems;

    /**
     * This Constructs an empty DLStack.
     * The top is set to null, and the number of items is initialized to 0.
     */
    public DLStack() {
        top = null;
        numItems = 0;
    }

    /**
     * Pushes an element onto the top of the stack.
     * This operation adds a new node with the given element to the top of the double-linked list. This method will
     * first check if the stack is empty. If it's it will assign it to the top of the stack. Else it will still add it
     * to the top of the stack.
     *
     * @param element The element to be pushed onto the stack.
     */

    @Override
    public void push(T element) {
        DoubleLinkedNode<T> newNode = new DoubleLinkedNode<>(element); // Making a new stack.
        numItems++; // Increment of the stack by 1.

        if (top == null) { // Checks to see if it is the first element being added to the stack.
            top = newNode;
        }
        else { // Else it will set the next top to the next element and the new element will become the top element.
        newNode.setPrevious(top);
        top.setNext(newNode);
        top = newNode;
        }
    }


    /**
     * This method removes and returns the element at the top of the double-linked stack.
     * If the stack is empty, it throws an EmptyStackException indicating that the stack is empty and there is no element to pop.
     * If there is only one element in the stack, it is removed, and the stack becomes empty.
     * Otherwise, the previous node becomes the new top of the stack, and the removed node's links are adjusted to maintain the doubly linked structure.
     * The number of items in the stack is decremented after successful removal.
     *
     * @return The element at the top of the stack that is removed.
     * @throws EmptyStackException if the stack is empty and there is no element to pop.
     */
    @Override
    public T pop() throws EmptyStackException {
        if (top == null) {
            throw new EmptyStackException("Empty Stack"); // Throws an empty stack exception if stack is empty.
        }
        if (top.getPrevious() == null) { // This condition will check if the stack only has one element left
            DoubleLinkedNode<T> copyOfTop = top;
            numItems--;
            top = null;
            return copyOfTop.getElement(); // Returns the last top.

        }
        DoubleLinkedNode<T> copyOfTop = top;  // Else it will remove the current top of the stack.
        top.getPrevious().setNext(null);
        top = top.getPrevious();
        numItems--;
        return copyOfTop.getElement();
    }

    /**
     * Removes and returns the element at position k from the top of the stack.

     * This method removes and returns the element at the specified position (k) from the top of the stack.
     * The position is counted from the top of the stack, starting with 1. For example, k=1 represents the top element.
     * If the stack is empty, this method throws an EmptyStackException.
     * If the specified position (k) is invalid (greater than the number of items in the stack or less than or equal to 0),
     * an InvalidItemException is thrown.
     * The method updates the references of the adjacent nodes to remove the element and adjusts the top accordingly.
     * The number of items in the stack is decremented after a successful removal.
     * Note: The method assumes that the stack contains at least k elements to avoid IndexOutOfBoundsException.
     *
     * @param k The position of the element to be removed, counting from the top of the stack starting with 1.
     * @return The element removed from the specified position in the stack.
     * @throws InvalidItemException If the given position k is greater than the number of items in the stack or less than or equal to 0.
     */
    @Override
    public T pop(int k) throws InvalidItemException {
        // Check if the given position (k) is within the valid range.
        if (k > numItems || k <= 0) {
            throw new InvalidItemException("InvalidItemException");
        }
        DoubleLinkedNode<T> nodeToRemoveAndReturn = top; // temp node
        int positionFromTop = 1; // Position from the top of the stack, starts at 1.

        // Traverse the stack to find the element at the specified position (k).
        while (positionFromTop != k) {
            positionFromTop++;
            nodeToRemoveAndReturn = nodeToRemoveAndReturn.getPrevious();
        }

        // Check if the element to remove is the top element.
        if (nodeToRemoveAndReturn == top) {
            // Update references to remove the element and adjust the top.
            nodeToRemoveAndReturn.getPrevious().setNext(nodeToRemoveAndReturn.getNext());
            top = nodeToRemoveAndReturn.getPrevious();
        }
        else if (nodeToRemoveAndReturn.getPrevious() == null) {
            // Update references to remove the element (when it's the bottom element).
            nodeToRemoveAndReturn.getNext().setPrevious(nodeToRemoveAndReturn.getPrevious());

        }
        else {
            // Update references to remove the element from the middle of the stack.
            nodeToRemoveAndReturn.getPrevious().setNext(nodeToRemoveAndReturn.getNext());
            nodeToRemoveAndReturn.getNext().setPrevious(nodeToRemoveAndReturn.getPrevious());
        }

        numItems--; // Decrement the number of items in the stack after successful removal.
        return nodeToRemoveAndReturn.getElement();
    }

    /**
     * The peek method will view and returns the value stored at the top node but will not remove it.
     * @return Returns the element of the top.
     * @throws EmptyStackException if the stack is empty.
     */
    @Override
    public T peek() throws EmptyStackException{
        if (top == null) { // Empty stack.
            throw new EmptyStackException("Empty Stack");
        }
        return top.getElement(); // Returns the element of the top node.
    }

    /**
     * This method checks to sse if the stack is empty.
     * @return Returns Ture if it is empty else it will return False.
     */
    @Override
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * This method will return the size of the stack.
     * @return Returns the size of the stack.
     */
    @Override
    public int size() {
        return numItems;
    }

    /**
     * This method will return the top node of the stack.
     * @return Returns top node.
     */
    @Override
    public DoubleLinkedNode<T> getTop() {
        return top;
    }

    /**
     * This method will return a string format presentation of the stack.
     * @return Returns string format of the stack.
     */
    public String toString() {
        String stringFormat = "";
        DoubleLinkedNode<T> tempNode = top;

        while (tempNode != null) {
            if (tempNode.getPrevious() == null) {
                stringFormat += tempNode.getElement();
                break;
            }
            stringFormat += tempNode.getElement() + ", ";
            tempNode = tempNode.getPrevious();
        }
        return "[" + stringFormat + "]";

    }
}
