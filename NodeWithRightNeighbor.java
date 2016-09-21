// This is the text editor interface. 
// Anything you type or change here will be seen by the other person in real time.

// Just testing stdio

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

// This is the text editor interface. 
// Anything you type or change here will be seen by the other person in real time.
class NodeWithRightNeighbor {
	private NodeWithRightNeighbor leftNode;
	private NodeWithRightNeighbor rightNode;
	private NodeWithRightNeighbor neighborNode;
	private Integer depth;
	private final long id;
	private static final AtomicLong idCounter = new AtomicLong(0);

	public NodeWithRightNeighbor(NodeWithRightNeighbor leftNode, NodeWithRightNeighbor rightNode) {
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.id = idCounter.getAndIncrement();
	}

	// public static void populateNeighborNodes(Node rootNode, Node parentNode){
	// // Currently has some children, but neighbors are null
	// if(rootNode.leftNode != null){
	// // rightNode may not be populated.
	// if(rootNode.rightNode != null){
	// rootNode.leftNode.neighborNode = rootNode.rightNode;
	// } else {
	// if(parentNode != null){
	// // ask parent for neighbor.
	// parentNode.get
	// }
	// }
	// populateNeighborNodes(rootNode.leftNode, rootNode);
	// }
	// if(rootNode.rightNode != null){
	// populateNeighborNodes(rootNode.rightNode, rootNode);
	// }
	// }

	public void resetDepths() {
		this.depth = null;
		if (leftNode != null) {
			leftNode.resetDepths();
		}
		if (rightNode != null) {
			rightNode.resetDepths();
		}
	}

	// O(n)
	public static void populateNeighborNodes(NodeWithRightNeighbor rootNode) {
		rootNode.resetDepths(); // For consistency of the nodes. Also, this algorithm depends on null depths to start. (Someone may attach child nodes to other nodes)
		Integer depth = 0;
		rootNode.depth = 0;
		Map<Integer, List<NodeWithRightNeighbor>> nodesByDepth = new HashMap<>();
		NodeWithRightNeighbor currentNode = rootNode;
		// O(n) - Hits each tree node exactly once
		Queue<NodeWithRightNeighbor> nodesToProcess = new ArrayDeque<>();
		while (currentNode != null) {
			depth = currentNode.depth;
			depth++;
			List<NodeWithRightNeighbor> nodes = nodesByDepth.get(depth);
			if (nodes == null) {
				nodes = new ArrayList<>();
				nodesByDepth.put(depth, nodes);
			}
			NodeWithRightNeighbor leftNode = currentNode.leftNode;
			NodeWithRightNeighbor rightNode = currentNode.rightNode;
			// Revisit this below, perhaps?
			if (leftNode != null) {
				leftNode.depth = depth;
				nodesToProcess.add(leftNode);
				nodes.add(leftNode);
			}
			if (rightNode != null) {
				rightNode.depth = depth;
				nodesToProcess.add(rightNode);
				nodes.add(rightNode);
			}
			currentNode = nodesToProcess.poll();
		}
		// O(n) - Hits each tree node exactly once
		for (Integer nodeDepth : nodesByDepth.keySet()) {
			List<NodeWithRightNeighbor> nodes = nodesByDepth.get(nodeDepth);
			// Each of these are neighbors.
			NodeWithRightNeighbor leftNode = null;
			for (NodeWithRightNeighbor node : nodes) {
				if (leftNode != null) {
					leftNode.neighborNode = node;
				}
				leftNode = node;
			}
		}
	}

	/* Kind of lame, but at least gives some visual of the structure */
	@Override
	public String toString() {
		String output = "id = " + id + ", depth = " + depth;
		if (neighborNode != null) {
			output += ", neighbor = " + neighborNode.id;
		}
		output += ", L = ";
		if (leftNode != null) {
			output += leftNode.id;
		}
		output += ", R = ";
		if (rightNode != null) {
			output += rightNode.id;
		}
		output += "\n";
		if (leftNode != null) {
			output += leftNode;
		}
		if (rightNode != null) {
			output += rightNode;
		}

		return output;
	}

	@Override
	public boolean equals(Object object) {
		return this == object;// Just a simple comparison.
	}

	public static void assertEqualsAndPrint(NodeWithRightNeighbor node1, NodeWithRightNeighbor node2) {
		System.out.print("Comparing: node1 = [");
		if (node1 != null) {
			System.out.print(node1.id);
		} else {
			System.out.print("null");
		}
		System.out.print("] to node2 = [");
		if (node2 != null) {
			System.out.print(node2.id);
		} else {
			System.out.print("null");
		}
		System.out.println("]");
		if (node1 != null || node2 != null) {
			if (node1 != null) {
				assert node1.equals(node2);
			} else {
				// node1 is null and node2 is not... Error.
				assert false;
			}
		} else {
			// null == null, no problem.
		}
	}

	public static void populateNeighborNodesAndPrint(NodeWithRightNeighbor node) {
		populateNeighborNodes(node);
		System.out.println(node);
	}

	// Add some basic unit tests... Printing output for review.
	public static void testNullRootNode() {
		System.out.println("Test: testNullRootNode");
		idCounter.set(0);
		populateNeighborNodesAndPrint(new NodeWithRightNeighbor(null, null)); // Works
	}

	public static void testSimpleRootNode() {
		System.out.println("Test: testSimpleRootNode");
		idCounter.set(0);
		NodeWithRightNeighbor simpleNode = new NodeWithRightNeighbor(new NodeWithRightNeighbor(null, null), new NodeWithRightNeighbor(null, null));
		populateNeighborNodesAndPrint(simpleNode);// Works
		assertEqualsAndPrint(simpleNode.leftNode.neighborNode, simpleNode.rightNode);
		assert simpleNode.leftNode.depth == 1;
		assert simpleNode.rightNode.depth == 1;
	}

	/*
	 * 0 ------ | | L1 null ---- | | null R2 ----- | | L3 null ---- | | L4->R4
	 */
	public static void testDeeperNode() {
		System.out.println("Test: testDeeperNode");
		idCounter.set(0);
		NodeWithRightNeighbor L4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor R4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor L3 = new NodeWithRightNeighbor(L4, R4);
		NodeWithRightNeighbor R2 = new NodeWithRightNeighbor(L3, null);
		NodeWithRightNeighbor L1 = new NodeWithRightNeighbor(null, R2);
		NodeWithRightNeighbor rootNode = new NodeWithRightNeighbor(L1, null);
		populateNeighborNodesAndPrint(rootNode);// Works
		// Check L1 tree structure
		assertEqualsAndPrint(rootNode.leftNode, L1);
		assertEqualsAndPrint(rootNode.leftNode.rightNode, R2);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode, L3);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode.rightNode, R4);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode.leftNode, L4);
		// Check neighbors
		assertEqualsAndPrint(L1.neighborNode, null);
		assertEqualsAndPrint(R2.neighborNode, null);
		assertEqualsAndPrint(L3.neighborNode, null);
		assertEqualsAndPrint(L4.neighborNode, R4);
		assertEqualsAndPrint(R4.neighborNode, null);
		// Check depths
		assert rootNode.depth == 0;
		assert L1.depth == 1;
		assert R2.depth == 2;
		assert L3.depth == 3;
		assert L4.depth == 4;
		assert R4.depth == 4;
	}

	public static void testLeftChildrenNodes(int totalDepth) {
		System.out.println("Test: testLeftChildrenNodes with " + totalDepth);
		totalDepth--;
		idCounter.set(0);
		NodeWithRightNeighbor leftNode = new NodeWithRightNeighbor(null, null);
		for (int i = 0; i < totalDepth; i++) {
			leftNode = new NodeWithRightNeighbor(leftNode, null);
		}
		NodeWithRightNeighbor rootNode = new NodeWithRightNeighbor(leftNode, null);
		populateNeighborNodesAndPrint(rootNode);// Works
		leftNode = rootNode.leftNode;
		for (int i = 0; i < totalDepth; i++) {
			assertEqualsAndPrint(leftNode.neighborNode, null);
			assert leftNode.depth == i + 1;
			leftNode = leftNode.leftNode;
		}
		assert leftNode.leftNode == null;
	}

	public static void testRightChildrenNodes(int totalDepth) {
		System.out.println("Test: testRightChildrenNodes with " + totalDepth);
		totalDepth--;
		idCounter.set(0);
		NodeWithRightNeighbor rightNode = new NodeWithRightNeighbor(null, null);
		for (int i = 0; i < totalDepth; i++) {
			rightNode = new NodeWithRightNeighbor(null, rightNode);
		}
		NodeWithRightNeighbor rootNode = new NodeWithRightNeighbor(null, rightNode);
		populateNeighborNodesAndPrint(rootNode);// Works
		rightNode = rootNode.rightNode;
		for (int i = 0; i < totalDepth; i++) {
			assertEqualsAndPrint(rightNode.neighborNode, null);
			assert rightNode.depth == i + 1;
			rightNode = rightNode.rightNode;
		}
		assert rightNode.rightNode == null;
	}

	public static void testLeftAndRightChildrenNode(int totalDepth) {
		System.out.println("Test: testLeftAndRightChildrenNode with " + totalDepth);
		totalDepth--;
		idCounter.set(0);
		NodeWithRightNeighbor leftNode = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor rightNode = new NodeWithRightNeighbor(null, null);
		for (int i = 0; i < totalDepth; i++) {
			leftNode = new NodeWithRightNeighbor(leftNode, null);
			rightNode = new NodeWithRightNeighbor(null, rightNode);
		}
		NodeWithRightNeighbor rootNode = new NodeWithRightNeighbor(leftNode, rightNode);
		populateNeighborNodesAndPrint(rootNode);// Works
		leftNode = rootNode.leftNode;
		rightNode = rootNode.rightNode;
		for (int i = 0; i < totalDepth; i++) {
			assertEqualsAndPrint(leftNode.neighborNode, rightNode);
			assertEqualsAndPrint(rightNode.neighborNode, null);
			assert leftNode.depth == i + 1;
			assert rightNode.depth == i + 1;
			leftNode = leftNode.leftNode;
			rightNode = rightNode.rightNode;
		}
		assert leftNode.leftNode == null;
		assert rightNode.rightNode == null;
	}

	/*
	 * 0 ------------------ | | L1 -> r1 ---- ------ | | | | null R2 -> l2 null ----- ----- | | | | L3 null null r3 (L3 -> r3) ---- ------ | | | | L4->R4 -> _l4->_r4
	 */
	public static void testComplexNode() {
		System.out.println("Test: testComplexNode");
		idCounter.set(0);
		NodeWithRightNeighbor L4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor R4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor L3 = new NodeWithRightNeighbor(L4, R4);
		NodeWithRightNeighbor R2 = new NodeWithRightNeighbor(L3, null);
		NodeWithRightNeighbor L1 = new NodeWithRightNeighbor(null, R2);
		NodeWithRightNeighbor _l4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor _r4 = new NodeWithRightNeighbor(null, null);
		NodeWithRightNeighbor r3 = new NodeWithRightNeighbor(_l4, _r4);
		NodeWithRightNeighbor l2 = new NodeWithRightNeighbor(null, r3);
		NodeWithRightNeighbor r1 = new NodeWithRightNeighbor(l2, null);
		NodeWithRightNeighbor rootNode = new NodeWithRightNeighbor(L1, r1);
		populateNeighborNodesAndPrint(rootNode);// Works
		// Check L1 tree structure
		assertEqualsAndPrint(rootNode.leftNode, L1);
		assertEqualsAndPrint(rootNode.leftNode.rightNode, R2);
		assertEqualsAndPrint(rootNode.leftNode.leftNode, null);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode, L3);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.rightNode, null);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode.rightNode, R4);
		assertEqualsAndPrint(rootNode.leftNode.rightNode.leftNode.leftNode, L4);
		// Check r1 tree structure
		assertEqualsAndPrint(rootNode.rightNode, r1);
		assertEqualsAndPrint(rootNode.rightNode.leftNode, l2);
		assertEqualsAndPrint(rootNode.rightNode.rightNode, null);
		assertEqualsAndPrint(rootNode.rightNode.leftNode.leftNode, null);
		assertEqualsAndPrint(rootNode.rightNode.leftNode.rightNode, r3);
		assertEqualsAndPrint(rootNode.rightNode.leftNode.rightNode.leftNode, _l4);
		assertEqualsAndPrint(rootNode.rightNode.leftNode.rightNode.rightNode, _r4);
		// Check neighbors
		assertEqualsAndPrint(L1.neighborNode, r1);
		assertEqualsAndPrint(r1.neighborNode, null);
		assertEqualsAndPrint(R2.neighborNode, l2);
		assertEqualsAndPrint(l2.neighborNode, null);
		assertEqualsAndPrint(L3.neighborNode, r3);
		assertEqualsAndPrint(r3.neighborNode, null);
		assertEqualsAndPrint(L4.neighborNode, R4);
		assertEqualsAndPrint(R4.neighborNode, _l4);
		assertEqualsAndPrint(_l4.neighborNode, _r4);
		assertEqualsAndPrint(_r4.neighborNode, null);
		// Check depths
		assert rootNode.depth == 0;
		assert L1.depth == 1;
		assert r1.depth == 1;
		assert l2.depth == 2;
		assert R2.depth == 2;
		assert L3.depth == 3;
		assert r3.depth == 3;
		assert L4.depth == 4;
		assert R4.depth == 4;
		assert _l4.depth == 4;
		assert _r4.depth == 4;
	}

	public static void main(String[] args) {
		testNullRootNode();
		testSimpleRootNode();
		testDeeperNode();
		testLeftChildrenNodes(8);
		testRightChildrenNodes(8);
		testLeftAndRightChildrenNode(8);
		// testLeftAndRightChildrenNode(1000);
		testComplexNode();
	}
}