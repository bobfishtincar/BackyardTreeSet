package bdinakar.backyardtreeset;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class BackyardTreeSet<T extends Comparable<T>> implements Set<T>, Serializable {

    /* Root node. */
    private NullTreeSetNode root;

    /** Prints out NullTreeSet. */
    public void print() {
        if (root == null) {
            System.out.println("Empty Set.");
        } else {
            root.print(0);
        }
    }

    @Override
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.size;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty()) {
            return false;
        } else {
            try {
                T t = (T) o;
                return root.contains(t);
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    // TODO
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException("Cannot add null items.");
        } else if (root == null) {
            root = new NullTreeSetNode(t);
            return true;
        } else if (root.contains(t)) {
            return false;
        } else {
            return root.add(t);
        }
    }

    // TODO
    @Override
    public boolean remove(Object o) {
        return false;
    }

    // TODO
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // TODO
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    // TODO
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    // TODO
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    private class NullTreeSetNode {

        /* Item stored in node. */
        private T item;
        /* Number of nodes linked to node. */
        private int size;
        /* Neighbors. */
        private NullTreeSetNode parent, left, right;

        /** Constructor with item input. */
        public NullTreeSetNode(T item) {
            this(item, null, null, null);
        }

        /** Constructor with item and parent inputs. */
        public NullTreeSetNode(T item, NullTreeSetNode parent) {
            this(item, parent, null, null);
        }

        /** Constructor with item, parent, and children inputs. */
        public NullTreeSetNode(T item, NullTreeSetNode parent,
                               NullTreeSetNode left, NullTreeSetNode right) {
            this.item = item;
            this.size = 1;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        /** Recursively prints out tree. */
        private void print(int depth) {
            int newDepth = depth + 1;
            if (right != null) {
                right.print(newDepth);
            }
            for (int i = 0; i < depth; i += 1) {
                System.out.print("\t");
            }
            System.out.println(item);
            if (left != null) {
                left.print(newDepth);
            }
        }

        /** Adds item to NullTreeSet. Returns true if item not originally in NulLTreeSet. */
        private boolean add(T t) {
            int comparison = t.compareTo(item);
            if (comparison < 0) {
                if (left == null) {
                    left = new NullTreeSetNode(t, this);
                    size += 1;
                    return true;
                } else if (right != null && left.size <= right.size) {
                    size += 1;
                    return left.add(t);
                } else {
                    NullTreeSetNode inOrderPre = left.getInOrderPre();
                    int inOrderComparison = t.compareTo(inOrderPre.item);
                    if (inOrderComparison == 0) {
                        return true;
                    } else if (inOrderComparison > 0) {
                        T oldItem = item;
                        item = t;
                        return add(oldItem);
                    } else {
                        T inOrderPreItem = ripPre(inOrderPre);
                        T oldItem = item;
                        item = inOrderPreItem;
                        add(oldItem);
                        incrementParentSize();
                        return add(t);
                    }
                }
            } else {
                if (right == null) {
                    right = new NullTreeSetNode(t, this);
                    size += 1;
                    return true;
                } else if (left != null && right.size <= left.size) {
                    size += 1;
                    return right.add(t);
                } else {
                    NullTreeSetNode inOrderSuc = right.getInOrderSuc();
                    int inOrderComparison = t.compareTo(inOrderSuc.item);
                    if (inOrderComparison == 0) {
                        return true;
                    } else if (inOrderComparison < 0) {
                        T oldItem = item;
                        item = t;
                        return add(oldItem);
                    } else {
                        T inOrderSucItem = ripSuc(inOrderSuc);
                        T oldItem = item;
                        item = inOrderSucItem;
                        add(oldItem);
                        incrementParentSize();
                        return add(t);
                    }
                }
            }
        }

        /** Rips inorder predecessor node and returns its item. */
        private T ripPre(NullTreeSetNode node) {
            if (node.left != null) {
                node.left.parent = node.parent;
            }
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else if (node == node.parent.right) {
                node.parent.right = node.left;
            }
            node.decrementParentSize();
            return node.item;
        }

        /** Rips inorder successor node and returns its item. */
        private T ripSuc(NullTreeSetNode node) {
            if (node.right != null) {
                node.right.parent = node.parent;
            }
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else if (node == node.parent.right) {
                node.parent.right = node.right;
            }
            node.decrementParentSize();
            return node.item;
        }

        private void incrementParentSize() {
            if (parent != null) {
                parent.size += 1;
                parent.incrementParentSize();
            }
        }

        private void decrementParentSize() {
            if (parent != null) {
                parent.size -= 1;
                parent.decrementParentSize();
            }
        }

        /** Returns inorder predecessor NullSetTreeNode. */
        private NullTreeSetNode getInOrderPre() {
            if (right == null) {
                return this;
            } else {
                return right.getInOrderPre();
            }
        }

        /** Returns inorder sucessor NullSetTreeNode. */
        private NullTreeSetNode getInOrderSuc() {
            if (left == null) {
                return this;
            } else {
                return left.getInOrderSuc();
            }
        }

        private boolean contains(T o) {
            if (item.equals(o)) {
                return true;
            } else {
                int comparison = item.compareTo(o);
                if (comparison > 0) {
                    return left != null
                            && left.contains(o);
                } else {
                    return right != null
                            && right.contains(o);
                }
            }
        }
    }

}
