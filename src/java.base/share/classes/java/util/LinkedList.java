/*
 * Copyright (c) 1997, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.util;

import java.util.function.Consumer;

/**
 * Doubly-linked list implementation of the {@code List} and {@code Deque}
 * interfaces.  Implements all optional list operations, and permits all
 * elements (including {@code null}).
 *
 * <p>All of the operations perform as could be expected for a doubly-linked
 * list.  Operations that index into the list will traverse the list from
 * the beginning or the end, whichever is closer to the specified index.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a linked list concurrently, and at least
 * one of the threads modifies the list structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more elements; merely setting the value of
 * an element is not a structural modification.)  This is typically
 * accomplished by synchronizing on some object that naturally
 * encapsulates the list.
 *
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new LinkedList(...));</pre>
 *
 * <p>The iterators returned by this class's {@code iterator} and
 * {@code listIterator} methods are <i>fail-fast</i>: if the list is
 * structurally modified at any time after the iterator is created, in
 * any way except through the Iterator's own {@code remove} or
 * {@code add} methods, the iterator will throw a {@link
 * ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than
 * risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:   <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @see     List
 * @see     ArrayList
 * @since 1.2
 * @param <E> the type of elements held in this collection
 */

public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{

    /**
     * 链表大小
     */
    transient int size = 0;

    /**
     * 头节点
     *
     * Pointer to first node.
     */
    transient Node<E> first;

    /**
     * 尾节点
     *
     * Pointer to last node.
     */
    transient Node<E> last;

    /*
    void dataStructureInvariants() {
        assert (size == 0)
            ? (first == null && last == null)
            : (first.prev == null && last.next == null);
    }
    */

    /**
     * Constructs an empty list.
     */
    public LinkedList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param  c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public LinkedList(Collection<? extends E> c) {
        this();
        // 添加 c 到链表中
        addAll(c);
    }

    /**
     * Links e as first element.
     */
    private void linkFirst(E e) {
        // 记录原 first 节点
        final Node<E> f = first;
        // 创建新节点
        final Node<E> newNode = new Node<>(null, e, f);
        // first 指向新节点
        first = newNode;
        // 如果原 first 为空，说明 last 也为空，则 last 也指向新节点
        if (f == null)
            last = newNode;
        // 如果原 first 非空，说明 last 也非空，则原 first 的 next 指向新节点。
        else
            f.prev = newNode;
        // 增加链表大小
        size++;
        // 增加数组修改次数
        modCount++;
    }

    /**
     * Links e as last element.
     */
    void linkLast(E e) {
        // 记录原 last 节点
        final Node<E> l = last;
        // 创建新节点
        final Node<E> newNode = new Node<>(l, e, null);
        // last 指向新节点
        last = newNode;
        // 如果原 last 为 null ，说明 fast 也为空，则 fast 也指向新节点
        if (l == null)
            first = newNode;
        // 如果原 last 非 null ，说明 fast 也非空，则原 last 的 next 指向新节点。
        else
            l.next = newNode;
        // 增加链表大小
        size++;
        // 增加数组修改次数
        modCount++;
    }

    /**
     * Inserts element e before non-null Node succ.
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        // 获得 succ 的前一个节点
        final Node<E> pred = succ.prev;
        // 创建新的节点 newNode
        final Node<E> newNode = new Node<>(pred, e, succ);
        // 设置 succ 的前一个节点为新节点
        succ.prev = newNode;
        // 如果 pred 为 null ，说明 first 也为空，则 first 也指向新节点
        if (pred == null)
            first = newNode;
        // 如果 pred 非 null ，说明 first 也为空，则 pred 也指向新节点
        else
            pred.next = newNode;
        // 增加链表大小
        size++;
        // 增加数组修改次数
        modCount++;
    }

    /**
     * Unlinks non-null first node f.
     */
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;
        // 获得 f 的下一个节点
        final Node<E> next = f.next;
        // 设置 f 的 item 为 null ，帮助 GC
        f.item = null;
        // 设置 f 的 next 为 null ，帮助 GC
        f.next = null; // help GC
        // 修改 fisrt 指向 next
        first = next;
        // 修改 next 节点的 prev 指向 null
        if (next == null) // 如果链表只有一个元素，说明被移除后，队列就是空的，则 last 设置为 null
            last = null;
        else
            next.prev = null;
        // 链表大小减一
        size--;
        // 增加数组修改次数
        modCount++;
        return element;
    }

    /**
     * Unlinks non-null last node l.
     */
    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.item;
        // 获得 f 的上一个节点
        final Node<E> prev = l.prev;
        // 设置 l 的 item 为 null ，帮助 GC
        l.item = null;
        // 设置 l 的 prev 为 null ，帮助 GC
        l.prev = null; // help GC
        // 修改 last 指向 prev
        last = prev;
        // 修改 prev 节点的 next 指向 null
        if (prev == null) // 如果链表只有一个元素，说明被移除后，队列就是空的，则 first 设置为 null
            first = null;
        else
            prev.next = null;
        // 链表大小减一
        size--;
        // 增加数组修改次数
        modCount++;
        return element;
    }

    /**
     * Unlinks non-null node x.
     */
    E unlink(Node<E> x) {
        // assert x != null;
        // 获得 x 的前后节点 prev、next
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        // 将 prev 的 next 指向下一个节点
        if (prev == null) { // 如果 prev 为空，说明 first 被移除，则直接将 first 指向 next
            first = next;
        } else { // 如果 prev 非空
            prev.next = next; // prev 的 next 指向 next
            x.prev = null; // x 的 pre 指向 null
        }

        // 将 next 的 prev 指向上一个节点
        if (next == null) { // 如果 next 为空，说明 last 被移除，则直接将 last 指向 prev
            last = prev;
        } else { // 如果 next 非空
            next.prev = prev; // next 的 prev 指向 prev
            x.next = null; // x 的 next 指向 null
        }

        // 将 x 的 item 设置为 null ，帮助 GC
        x.item = null;
        // 减少链表大小
        size--;
        // 增加数组的修改次数
        modCount++;
        return element;
    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getFirst() {
        final Node<E> f = first;
        if (f == null) // 如果链表为空识，抛出 NoSuchElementException 异常
            throw new NoSuchElementException();
        return f.item;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeFirst() {
        final Node<E> f = first;
        // 如果链表为空，抛出 NoSuchElementException 异常
        if (f == null)
            throw new NoSuchElementException();
        // 移除链表时首个元素
        return unlinkFirst(f);
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeLast() {
        final Node<E> l = last;
        // 如果链表为空，则抛出 NoSuchElementException 移除
        if (l == null)
            throw new NoSuchElementException();
        // 移除链表的最后一个元素
        return unlinkLast(l);
    }

    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e the element to add
     */
    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    /**
     * Returns {@code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list contains
     * at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this list is to be tested
     * @return {@code true} if this list contains the specified element
     */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * <p>This method is equivalent to {@link #addLast}.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        // 添加末尾
        linkLast(e);
        return true;
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If this list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * {@code i} such that
     * {@code Objects.equals(o, get(i))}
     * (if such an element exists).  Returns {@code true} if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     */
    public boolean remove(Object o) {
        if (o == null) { // o 为 null 的情况
            // 顺序遍历，找到 null 的元素后，进行移除
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            // 顺序遍历，找到等于 o 的元素后，进行移除
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator.  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress.  (Note that this will occur if the specified collection is
     * this list, and it's nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element
     *              from the specified collection
     * @param c collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index);

        // 将 c 转成 a 数组
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) // 如果无添加元素，直接返回 false 数组未变更
            return false;

        // 获得第 index 位置的节点 succ ，和其前一个节点 pred
        Node<E> pred, succ;
        if (index == size) { // 如果 index 就是链表大小，那说明插入队尾，所以 succ 为 null ，pred 为 last 。
            succ = null;
            pred = last;
        } else { // 如果 index 小于链表大小，则 succ 是第 index 个节点，prev 是 succ 的前一个二节点。
            succ = node(index);
            pred = succ.prev;
        }

        // 遍历 a 数组，添加到 pred 的后面
        for (Object o : a) {
            // 创建新节点
            @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null);
            // 如果 pred 为 null ，说明 first 也为 null ，则直接将 first 指向新节点
            if (pred == null)
                first = newNode;
            // pred 下一个指向新节点
            else
                pred.next = newNode;
            // 修改 pred 指向新节点
            pred = newNode;
        }

        // 修改 succ 和 pred 的指向
        if (succ == null) { // 如果 succ 为 null ，说明插入队尾，则直接修改 last 指向最后一个 pred
            last = pred;
        } else { // 如果 succ 非 null ，说明插入到 succ 的前面
            pred.next = succ; // prev 下一个指向 succ
            succ.prev = pred; // succes 前一个指向 pred
        }

        // 增加链表大小
        size += numNew;
        // 增加数组修改次数
        modCount++;
        // 返回 true 数组有变更
        return true;
    }

    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns.
     */
    public void clear() {
        // Clearing all of the links between nodes is "unnecessary", but:
        // - helps a generational GC if the discarded nodes inhabit
        //   more than one generation
        // - is sure to free memory even if there is a reachable Iterator
        // 顺序遍历链表，设置每个节点前后指向为 null
        // 通过这样的方式，帮助 GC
        for (Node<E> x = first; x != null; ) {
            // 获得下一个节点
            Node<E> next = x.next;
            // 设置 x 的 item、next、prev 为空。
            x.item = null;
            x.next = null;
            x.prev = null;
            // 设置 x 为下一个节点
            x = next;
        }
        // 清空 first 和 last 指向
        first = last = null;
        // 设置链表大小为 0
        size = 0;
        // 增加数组修改次数
        modCount++;
    }

    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        checkElementIndex(index);
        // 基于 node(int index) 方法实现
        return node(index).item;
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        // 获得第 index 位置的节点
        Node<E> x = node(index);
        E oldVal = x.item;
        // 修改对应的值
        x.item = element;
        return oldVal;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        // 校验不要超过范围
        checkPositionIndex(index);

        // 如果刚好等于链表大小，直接添加到尾部即可
        if (index == size)
            linkLast(element);
        // 添加到第 index 的节点的前面
        else
            linkBefore(element, node(index));
    }

    /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        checkElementIndex(index);
        // 获得第 index 的 Node 节点，然后进行移除。
        return unlink(node(index));
    }

    /**
     * Tells if the argument is the index of an existing element.
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * Tells if the argument is the index of a valid position for an
     * iterator or an add operation.
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Returns the (non-null) Node at the specified element index.
     */
    Node<E> node(int index) {
        // assert isElementIndex(index);

        // 如果 index 小于 size 的一半，就正序遍历，获得第 index 个节点
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        // 如果 index 大于 size 的一半，就倒序遍历，获得第 index 个节点
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // Search Operations

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) { // 如果 o 为 null 的情况
            // 顺序遍历，如果 item 为 null 的节点，进行返回
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index; // 找到
                index++;
            }
        } else { // 如果 o 非 null 的情况
            // 顺序遍历，如果 item 为 o 的节点，进行返回
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index; // 找到
                index++;
            }
        }
        // 未找到
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the last occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) { // 如果 o 为 null 的情况
            // 倒序遍历，如果 item 为 null 的节点，进行返回
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index; // 找到
            }
        } else { // 如果 o 非 null 的情况
            // 倒序遍历，如果 item 为 o 的节点，进行返回
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index; // 找到
            }
        }
        // 未找到
        return -1;
    }

    // Queue operations.

    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     *
     * @return the head of this list, or {@code null} if this list is empty
     * @since 1.5
     */
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     *
     * @return the head of this list
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public E element() {
        return getFirst();
    }

    /**
     * Retrieves and removes the head (first element) of this list.
     *
     * @return the head of this list, or {@code null} if this list is empty
     * @since 1.5
     */
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * Retrieves and removes the head (first element) of this list.
     *
     * @return the head of this list
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    @Override
    public E remove() {
        return removeFirst();
    }

    /**
     * Adds the specified element as the tail (last element) of this list.
     *
     * @param e the element to add
     * @return {@code true} (as specified by {@link Queue#offer})
     * @since 1.5
     */
    public boolean offer(E e) {
        return add(e);
    }

    // Deque operations
    /**
     * Inserts the specified element at the front of this list.
     *
     * @param e the element to insert
     * @return {@code true} (as specified by {@link Deque#offerFirst})
     * @since 1.6
     */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Inserts the specified element at the end of this list.
     *
     * @param e the element to insert
     * @return {@code true} (as specified by {@link Deque#offerLast})
     * @since 1.6
     */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Retrieves, but does not remove, the first element of this list,
     * or returns {@code null} if this list is empty.
     *
     * @return the first element of this list, or {@code null}
     *         if this list is empty
     * @since 1.6
     */
    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
     }

    /**
     * Retrieves, but does not remove, the last element of this list,
     * or returns {@code null} if this list is empty.
     *
     * @return the last element of this list, or {@code null}
     *         if this list is empty
     * @since 1.6
     */
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }

    /**
     * Retrieves and removes the first element of this list,
     * or returns {@code null} if this list is empty.
     *
     * @return the first element of this list, or {@code null} if
     *     this list is empty
     * @since 1.6
     */
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * Retrieves and removes the last element of this list,
     * or returns {@code null} if this list is empty.
     *
     * @return the last element of this list, or {@code null} if
     *     this list is empty
     * @since 1.6
     */
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    /**
     * Pushes an element onto the stack represented by this list.  In other
     * words, inserts the element at the front of this list.
     *
     * <p>This method is equivalent to {@link #addFirst}.
     *
     * @param e the element to push
     * @since 1.6
     */
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Pops an element from the stack represented by this list.  In other
     * words, removes and returns the first element of this list.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the element at the front of this list (which is the top
     *         of the stack represented by this list)
     * @throws NoSuchElementException if this list is empty
     * @since 1.6
     */
    public E pop() {
        return removeFirst();
    }

    /**
     * Removes the first occurrence of the specified element in this
     * list (when traversing the list from head to tail).  If the list
     * does not contain the element, it is unchanged.
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if the list contained the specified element
     * @since 1.6
     */
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * Removes the last occurrence of the specified element in this
     * list (when traversing the list from head to tail).  If the list
     * does not contain the element, it is unchanged.
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if the list contained the specified element
     * @since 1.6
     */
    public boolean removeLastOccurrence(Object o) {
        if (o == null) { // o 为 null 的情况
            // 倒序遍历，找到 null 的元素后，进行移除
            for (Node<E> x = last; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            // 倒序遍历，找到等于 o 的元素后，进行移除
            for (Node<E> x = last; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * Obeys the general contract of {@code List.listIterator(int)}.<p>
     *
     * The list-iterator is <i>fail-fast</i>: if the list is structurally
     * modified at any time after the Iterator is created, in any way except
     * through the list-iterator's own {@code remove} or {@code add}
     * methods, the list-iterator will throw a
     * {@code ConcurrentModificationException}.  Thus, in the face of
     * concurrent modification, the iterator fails quickly and cleanly, rather
     * than risking arbitrary, non-deterministic behavior at an undetermined
     * time in the future.
     *
     * @param index index of the first element to be returned from the
     *              list-iterator (by a call to {@code next})
     * @return a ListIterator of the elements in this list (in proper
     *         sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see List#listIterator(int)
     */
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {

        /**
         * 最后返回的节点
         */
        private Node<E> lastReturned;
        /**
         * 下一个节点
         */
        private Node<E> next;
        /**
         * 下一个访问元素的位置，从下标 0 开始。
         *
         * 主要用于 {@link #nextIndex()} 中，判断是否遍历结束
         */
        private int nextIndex;
        /**
         * 创建迭代器时，数组修改次数。
         *
         * 在迭代过程中，如果数组发生了变化，会抛出 ConcurrentModificationException 异常。
         */
        private int expectedModCount = modCount;

        ListItr(int index) {
            // assert isPositionIndex(index);
            // 获得下一个节点
            next = (index == size) ? null : node(index);
            // 下一个节点的位置
            nextIndex = index;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public E next() {
            // 校验是否数组发生了变化
            checkForComodification();
            // 如果已经遍历到结尾，抛出 NoSuchElementException 异常
            if (!hasNext())
                throw new NoSuchElementException();

            // lastReturned 指向，记录最后访问节点
            lastReturned = next;
            // next 指向，下一个节点
            next = next.next;
            // 下一个节点的位置 + 1
            nextIndex++;
            // 返回 lastReturned
            return lastReturned.item;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            // 校验是否数组发生了变化
            checkForComodification();
            // 如果已经遍历到结尾，抛出 NoSuchElementException 异常
            if (!hasPrevious())
                throw new NoSuchElementException();

            // 修改 lastReturned 和 next 的指向。此时，lastReturned 和 next 是相等的。
            lastReturned = next = (next == null) ? last : next.prev;
            // 下一个节点的位置 - 1
            nextIndex--;
            // 返回 lastReturned
            return lastReturned.item;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            // 校验是否数组发生了变化
            checkForComodification();
            // 如果 lastReturned 为空，抛出 IllegalStateException 异常，因为无法移除了。
            if (lastReturned == null)
                throw new IllegalStateException();

            // 获得 lastReturned 的下一个
            Node<E> lastNext = lastReturned.next;
            // 移除 lastReturned 节点
            unlink(lastReturned);
            // 此处，会分成两种情况
            if (next == lastReturned) // 说明发生过调用 `#previous()` 方法的情况，next 指向下一个节点，而 nextIndex 是无需更改的
                next = lastNext;
            else
                nextIndex--; // nextIndex 减一。

            // 设置 lastReturned 为空
            lastReturned = null;
            // 增加数组修改次数
            expectedModCount++;
        }

        public void set(E e) {
            // 如果 lastReturned 为空，抛出 IllegalStateException 异常，因为无法修改了。
            if (lastReturned == null)
                throw new IllegalStateException();
            // 校验是否数组发生了变化
            checkForComodification();
            // 修改 lastReturned 的 item 为 e
            lastReturned.item = e;
        }

        public void add(E e) {
            // 校验是否数组发生了变化
            checkForComodification();
            // 设置 lastReturned 为空
            lastReturned = null;
            // 此处，会分成两种情况
            if (next == null) // 如果 next 已经遍历到尾，则 e 作为新的尾节点，进行插入。算是性能优化
                linkLast(e);
            else // 插入到 next 的前面
                linkBefore(e, next);
            // nextIndex 加一。
            nextIndex++;
            // 增加数组修改次数
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            // 遍历剩余链表
            while (modCount == expectedModCount && nextIndex < size) {
                // 执行 action 逻辑
                action.accept(next.item);
                // lastReturned 指向 next
                lastReturned = next;
                //  next 指向下一个节点
                next = next.next;
                // nextIndex 加一。
                nextIndex++;
            }
            // 校验是否数组发生了变化
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

    }

    /**
     * 节点
     *
     * @param <E> 元素泛型
     */
    private static class Node<E> {

        /**
         * 元素
         */
        E item;
        /**
         * 前一个节点
         */
        Node<E> next;
        /**
         * 后一个节点
         */
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    /**
     * @since 1.6
     */
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /**
     * Adapter to provide descending iterators via ListItr.previous
     */
    private class DescendingIterator implements Iterator<E> {
        private final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedList<E> superClone() {
        try {
            return (LinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Returns a shallow copy of this {@code LinkedList}. (The elements
     * themselves are not cloned.)
     *
     * @return a shallow copy of this {@code LinkedList} instance
     */
    public Object clone() {
        // 调用父类，进行克隆
        LinkedList<E> clone = superClone();

        // Put clone into "virgin" state
        // 重置 clone 为初始化状态
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        // Initialize clone with our elements
        // 遍历遍历，逐个添加到 clone 中
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list
     *         in proper sequence
     */
    public Object[] toArray() {
        // 创建 Object 数组
        Object[] result = new Object[size];
        // 顺序遍历节点，设置到 Object 数组中
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence (from first to last element); the runtime type of
     * the returned array is that of the specified array.  If the list fits
     * in the specified array, it is returned therein.  Otherwise, a new
     * array is allocated with the runtime type of the specified array and
     * the size of this list.
     *
     * <p>If the list fits in the specified array with room to spare (i.e.,
     * the array has more elements than the list), the element in the array
     * immediately following the end of the list is set to {@code null}.
     * (This is useful in determining the length of the list <i>only</i> if
     * the caller knows that the list does not contain any null elements.)
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a list known to contain only strings.
     * The following code can be used to dump the list into a newly
     * allocated array of {@code String}:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // <1> 如果传入的数组小于 size 大小，则直接复制一个新数组返回
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);
        // <2> 顺序遍历链表，复制到 a 中
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        // <2.1> 如果传入的数组大于 size 大小，则将 size 赋值为 null
        if (a.length > size)
            a[size] = null;

        // <2.2> 返回 a
        return a;
    }

    @java.io.Serial
    private static final long serialVersionUID = 876323262645176354L;

    /**
     * Saves the state of this {@code LinkedList} instance to a stream
     * (that is, serializes it).
     *
     * @serialData The size of the list (the number of elements it
     *             contains) is emitted (int), followed by all of its
     *             elements (each an Object) in the proper order.
     */
    @java.io.Serial
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        // Write out any hidden serialization magic
        // 写入非静态属性、非 transient 属性
        s.defaultWriteObject();

        // Write out size
        // 写入链表大小
        s.writeInt(size);

        // Write out all elements in the proper order.
        // 顺序遍历，逐个序列化
        for (Node<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }

    /**
     * Reconstitutes this {@code LinkedList} instance from a stream
     * (that is, deserializes it).
     */
    @SuppressWarnings("unchecked")
    @java.io.Serial
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        // 读取非静态属性、非 transient 属性
        s.defaultReadObject();

        // Read in size
        // 读取 size
        int size = s.readInt();

        // Read in all elements in the proper order.
        // 顺序遍历，逐个反序列化
        for (int i = 0; i < size; i++)
            linkLast((E)s.readObject()); // 添加到链表尾部
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#ORDERED}.  Overriding implementations should document
     * the reporting of additional characteristic values.
     *
     * @implNote
     * The {@code Spliterator} additionally reports {@link Spliterator#SUBSIZED}
     * and implements {@code trySplit} to permit limited parallelism..
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new LLSpliterator<>(this, -1, 0);
    }

    /** A customized variant of Spliterators.IteratorSpliterator */
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedList<E> list; // null OK unless traversed
        Node<E> current;      // current node; null until initialized
        int est;              // size estimate; -1 until first needed
        int expectedModCount; // initialized when est set
        int batch;            // batch size for splits

        LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final LinkedList<E> lst;
            if ((s = est) < 0) {
                if ((lst = list) == null)
                    s = est = 0;
                else {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize() { return (long) getEst(); }

        public Spliterator<E> trySplit() {
            Node<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do { a[j++] = p.item; } while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Node<E> p; int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Node<E> p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

}
