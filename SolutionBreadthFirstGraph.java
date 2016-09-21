import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SolutionBreadthFirstGraph {
	private static class Node {
		private int id;
		private int distanceFromParent;

		Node(int id) {
			this.id = id;
			this.distanceFromParent = 0;
		}

		@Override
		public int hashCode() {
			return id;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Node other = (Node) obj;
			if (id != other.id) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [id=");
			builder.append(id);
			builder.append(", distanceFromParent=");
			builder.append(distanceFromParent);
			builder.append("]");
			return builder.toString();
		}
	}

	public static void main(String[] args) throws IOException {
		/* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
		InputStream inputStream = System.in;
		if (args.length == 1) {
			inputStream = new FileInputStream(new File(args[0]));
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		Integer testCaseCount = Integer.parseInt(br.readLine());
		for (int test = 0; test < testCaseCount; test++) {
			String[] nodesAndEdges = br.readLine().split(" ");
			int nodes = Integer.parseInt(nodesAndEdges[0]);
			int edges = Integer.parseInt(nodesAndEdges[1]);
			Map<Integer, Set<Node>> graph = new HashMap<>();
			for (int i = 0; i < edges; i++) {
				String[] edgePoints = br.readLine().split(" ");
				int edgePoint0 = Integer.parseInt(edgePoints[0]);
				int edgePoint1 = Integer.parseInt(edgePoints[1]);
				Set<Node> set0 = graph.get(edgePoint0);
				if (set0 == null) {
					set0 = new HashSet<>();
					graph.put(edgePoint0, set0);
				}
				set0.add(new Node(edgePoint1));
				Set<Node> set1 = graph.get(edgePoint1);
				if (set1 == null) {
					set1 = new HashSet<>();
					graph.put(edgePoint1, set1);
				}
				set1.add(new Node(edgePoint0));
			}
			int startingNodeId = Integer.parseInt(br.readLine().trim());
			String out = "";
			for (int i = 1; i <= nodes; i++) {
				if (i == startingNodeId) {
					continue;
				}
				Queue<Node> queue = new ArrayDeque<>();
				Node startingNode = new Node(startingNodeId);
				queue.add(startingNode);
				int distanceFromParent = -1;
				Set<Node> processedNodes = new HashSet<>();
				processedNodes.add(startingNode);
				OUTER_LOOP: while (!queue.isEmpty()) {
					Node parentNode = queue.poll();
					Set<Node> childNodes = graph.get(parentNode.id);
					if (childNodes != null) {
						for (Node node : childNodes) {
							if (processedNodes.contains(node)) {
								continue;
							}
							node.distanceFromParent = parentNode.distanceFromParent + 1;
							// System.out.println(node);
							if (node.id == i) {
								if (distanceFromParent == -1) {
									distanceFromParent = node.distanceFromParent;
								} else {
									distanceFromParent = Math.min(distanceFromParent, node.distanceFromParent);
								}
							}
							if (node.distanceFromParent > 16 || (distanceFromParent != -1 && node.distanceFromParent >= distanceFromParent)) {
								continue OUTER_LOOP;
							}
							queue.add(node);
						}
						processedNodes.addAll(childNodes);
					}
				}
				out += distanceFromParent != -1 ? distanceFromParent * 6 : -1;
				if (i != nodes) {
					out += " ";
				}
			}
			System.out.println(out);
		}
	}
}