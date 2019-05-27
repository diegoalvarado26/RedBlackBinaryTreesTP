import java.io.Serializable;

public class RedBlackTree<T extends Comparable<T>> implements Serializable {

	private Node root;
	private Node TNULL;

	public RedBlackTree() {
		TNULL = new Node();
		TNULL.color = 0;
		TNULL.left = null;
		TNULL.right = null;
		root = TNULL;
	}

	public boolean nodeIsDead() {
		return root.isDead;
	}

	public boolean isEmpty() {
		return root.data == null;
	}

	public void compact() {
		searchDeadNode(this.root);
	}

	public void killNode(Comparable<T> k) {
		search(this.root, k).isDead = true;
	}

	private void searchDeadNode(Node node) {
		if (node != null) {
			if (node.isDead) {
				deleteNode(node.data);
			}
			searchDeadNode(node.left);
			searchDeadNode(node.right);
		}
	}

	private void fixDelete(Node x) {
		Node s;
		while (x != root && x.color == 0) {
			if (x == x.parent.left) {
				s = x.parent.right;
				if (s.color == 1) {
					s.color = 0;
					x.parent.color = 1;
					leftRotate(x.parent);
					s = x.parent.right;
				}

				if (s.left.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					if (s.right.color == 0) {
						s.left.color = 0;
						s.color = 1;
						rightRotate(s);
						s = x.parent.right;
					}

					s.color = x.parent.color;
					x.parent.color = 0;
					s.right.color = 0;
					leftRotate(x.parent);
					x = root;
				}
			} else {
				s = x.parent.left;
				if (s.color == 1) {
					// case 3.1
					s.color = 0;
					x.parent.color = 1;
					rightRotate(x.parent);
					s = x.parent.left;
				}

				if (s.right.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					if (s.left.color == 0) {
						s.right.color = 0;
						s.color = 1;
						leftRotate(s);
						s = x.parent.left;
					}
					s.color = x.parent.color;
					x.parent.color = 0;
					s.left.color = 0;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		x.color = 0;
	}

	// logicDelete the node from the tree
	public void deleteNode(Comparable<T> data) {
		deleteNodeHelper(this.root, data);
	}

	public RedBlackTree<T> getLeft() {
		RedBlackTree<T> r = new RedBlackTree<>();
		r.root = root.left;
		return r;
	}

	public RedBlackTree<T> getRight() {
		RedBlackTree<T> r = new RedBlackTree<>();
		r.root = root.right;
		return r;
	}

	private Node search(Node node, Comparable<T> key) {
		if (node == TNULL) {
			throw new RuntimeException("No element found...");
		}
		if (key.compareTo((T) node.data) == 0)
			return node;

		if (key.compareTo((T) node.data) < 0) {
			return search(node.left, key);
		}
		return search(node.right, key);
	}

	private void rbTransplant(Node u, Node v) {
		if (u.parent == null) {
			root = v;
		} else if (u == u.parent.left) {
			u.parent.left = v;
		} else {
			u.parent.right = v;
		}
		v.parent = u.parent;
	}

	private void deleteNodeHelper(Node node, Comparable<T> key) {
		Node z = TNULL;
		Node x, y;
		while (node != TNULL) {
			if (node.data == key) {
				z = node;
			}

			if (node.data.compareTo(key) <= 1) {
				node = node.right;
			} else {
				node = node.left;
			}
		}

		if (z == TNULL) {
			System.out.println("Couldn't find key in the tree");
			return;
		}

		y = z;
		int yOriginalColor = y.color;
		if (z.left == TNULL) {
			x = z.right;
			rbTransplant(z, z.right);
		} else if (z.right == TNULL) {
			x = z.left;
			rbTransplant(z, z.left);
		} else {
			y = minimum(z.right);
			yOriginalColor = y.color;
			x = y.right;
			if (y.parent == z) {
				x.parent = y;
			} else {
				rbTransplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}

			rbTransplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		if (yOriginalColor == 0) {
			fixDelete(x);
		}
	}

	private void fixInsert(Node k) {
		Node u;
		while (k.parent.color == 1) {
			if (k.parent == k.parent.parent.right) {
				u = k.parent.parent.left;
				if (u.color == 1) {
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.left) {
						k = k.parent;
						rightRotate(k);
					}
					// case 3.2.1
					k.parent.color = 0;
					k.parent.parent.color = 1;
					leftRotate(k.parent.parent);
				}
			} else {
				u = k.parent.parent.right;

				if (u.color == 1) {
					// mirror case 3.1
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.right) {
						k = k.parent;
						leftRotate(k);
					}
					k.parent.color = 0;
					k.parent.parent.color = 1;
					rightRotate(k.parent.parent);
				}
			}
			if (k == root) {
				break;
			}
		}
		root.color = 0;
	}

	public T search(Comparable<T> k) {
		return (T) search(this.root, k).data;
	}

	public Node minimum(Node node) {
		while (node.left != TNULL) {
			node = node.left;
		}
		return node;
	}

	public Node maximum(Node node) {
		while (node.right != TNULL) {
			node = node.right;
		}
		return node;
	}

	public Node successor(Node x) {
		if (x.right != TNULL) {
			return minimum(x.right);
		}

		Node y = x.parent;
		while (y != TNULL && x == y.right) {
			x = y;
			y = y.parent;
		}
		return y;
	}

	public Node predecessor(Node x) {
		if (x.left != TNULL) {
			return maximum(x.left);
		}

		Node y = x.parent;
		while (y != TNULL && x == y.left) {
			x = y;
			y = y.parent;
		}

		return y;
	}

	public void leftRotate(Node x) {
		Node y = x.right;
		x.right = y.left;
		if (y.left != TNULL) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.left) {
			x.parent.left = y;
		} else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	}

	public void rightRotate(Node x) {
		Node y = x.left;
		x.left = y.right;
		if (y.right != TNULL) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.right) {
			x.parent.right = y;
		} else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	}

	public void insert(Comparable<T> key) {
		Node node = new Node();
		node.parent = null;
		node.data = key;
		node.left = TNULL;
		node.right = TNULL;
		node.color = 1;

		Node y = null;
		Node x = this.root;

		while (x != TNULL) {
			y = x;
			if (node.data.compareTo(x.data) < 0) {
				x = x.left;
			} else {
				x = x.right;
			}
		}

		node.parent = y;
		if (y == null) {
			root = node;
		} else if (node.data.compareTo(y.data) < 0) {
			y.left = node;
		} else {
			y.right = node;
		}

		if (node.parent == null) {
			node.color = 0;
			return;
		}

		if (node.parent.parent == null) {
			return;
		}

		fixInsert(node);
	}

	public T getRoot() {
		return (T) this.root.data;
	}

	private class Node<T extends Comparable<T>> implements Serializable {
		Comparable<T> data;
		Node parent;
		Node left;
		Node right;
		int color;
		boolean isDead;
	}

}
