package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

/**
 * This implementation allow save state current position in the list.
 * <p>
 * This is as iterator, however this implementation save current position passed elements
 * in any time.
 * <p>
 * This implementation allow
 * <p>
 * <b>1) Receive current elements by current state position of the list</b>
 * <p>
 * <b>2) Remove current element by current state position on the list</b>
 * <p>
 * <b>3) Moved position </b>
 * <p>
 * 4) give random access to the elements by index.
 * <p>
 * 5) remove elements by index
 *
 * @apiNote current position not controlled if <b>remove</b> elements of the list.
 * So if remove any element from list, and the {@link #curIndex} will be  >= 0, then
 * will invoke method: {@link #get(int)} {@link #remove(int)} {@link #next()} {@link #get(int)} generate  {@link IndexOutOfBoundsException}
 */
public class WrapperList<E> extends AbstractList<E> implements Iterator<E> {
    private int curIndex;
    private final List<E> list;

    private WrapperList(List<E> list) {
        curIndex = 0;
        this.list = list;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public List<E> getList() {
        return list;
    }

    public void setCurIndex(int index) {
        this.curIndex = index;
    }

    public int improvePosPrefix() {
        return ++curIndex;
    }

    public int improvePosPostfix() {
        return curIndex++;
    }

    public void resetCurIndex() {
        curIndex = 0;
    }

    /**
     * Returns true if next would return an element rather than throwing an exception
     */
    @Override
    public boolean hasNext() {
        return curIndex != list.size();
    }

    /**
     * Return current element <b>by position.</b>
     * <p>
     * {@code Current position moved next}
     *
     * @throws ArrayIndexOutOfBoundsException if {@link #curIndex}  out bounds
     * @apiNote using method {@link #hasNext()} for avoiding exception
     */
    @Override
    public E next() {
        return list.get(curIndex++);
    }

    /**
     * Return element by <b>current position</b>
     * <p>
     * {@code Current position stay on the same place}
     *
     * @throws ArrayIndexOutOfBoundsException if {@link #curIndex}  out bounds
     * @apiNote using method {@link #hasNext()} for avoiding exception
     */
    public E current() {
        return list.get(curIndex);
    }

    /**
     * Remove element by <b>by current position</b>
     *
     * @throws ArrayIndexOutOfBoundsException if {@link #curIndex}  out bounds
     * @apiNote using method {@link #hasNext()} for avoiding exception
     */
    @Override
    public void remove() {
        list.remove(curIndex);
    }

    /**
     * Return element by given index
     */
    @Override
    public E get(int index) {
        return list.get(index);
    }

    /**
     * Remove element by given index
     */
    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    public static <E> WrapperList<E> of(List<E> list) {
        return new WrapperList<>(list);
    }
}
