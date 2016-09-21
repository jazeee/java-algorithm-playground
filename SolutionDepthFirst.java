import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolutionDepthFirst {
	private static final Map<Integer, Integer> MEMO = new HashMap<>();

	private static Integer memoedCountEdges(int startNode, int targetNode) {
		return MEMO.get(startNode * 10000 + targetNode);
	}

	private static void addMemo(int startNode, int targetNode, int value) {
		MEMO.put(startNode * 10000 + targetNode, value);
	}

	private static int countEdges(Map<Integer, Set<Integer>> graph, Set<Integer> usedNodes, int startNode, int targetNode) {
		Integer memoedCountEdges = memoedCountEdges(startNode, targetNode);
		if (memoedCountEdges != null) {
			return memoedCountEdges;
		}
		Set<Integer> connectedNodes = graph.get(startNode);
		if (connectedNodes == null) {
			addMemo(startNode, targetNode, -1);
			return -1;
		}
		if (connectedNodes.contains(targetNode)) {
			addMemo(startNode, targetNode, 1);
			return 1;
		}
		int shortestEdgeCount = -1;
		for (Integer connectedNode : connectedNodes) {
			if (!usedNodes.contains(connectedNode)) {
				Set<Integer> nextUsedNodes = new HashSet<>(usedNodes);
				nextUsedNodes.add(connectedNode);
				int edgeCount = countEdges(graph, nextUsedNodes, connectedNode, targetNode);
				if (edgeCount != -1) {
					if (shortestEdgeCount == -1) {
						shortestEdgeCount = 1 + edgeCount;
					} else {
						shortestEdgeCount = Math.min(shortestEdgeCount, 1 + edgeCount);
					}
					if (shortestEdgeCount == 2) {
						break;
					}
				}
			}
		}
		addMemo(startNode, targetNode, shortestEdgeCount);
		return shortestEdgeCount;
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
			MEMO.clear();
			String[] nodesAndEdges = br.readLine().split(" ");
			int nodes = Integer.parseInt(nodesAndEdges[0]);
			int edges = Integer.parseInt(nodesAndEdges[1]);
			Map<Integer, Set<Integer>> graph = new HashMap<>();
			for (int i = 0; i < edges; i++) {
				String[] edgePoints = br.readLine().split(" ");
				int edgePoint0 = Integer.parseInt(edgePoints[0]);
				int edgePoint1 = Integer.parseInt(edgePoints[1]);
				Set<Integer> set0 = graph.get(edgePoint0);
				if (set0 == null) {
					set0 = new HashSet<>();
					graph.put(edgePoint0, set0);
				}
				set0.add(edgePoint1);
				Set<Integer> set1 = graph.get(edgePoint1);
				if (set1 == null) {
					set1 = new HashSet<>();
					graph.put(edgePoint1, set1);
				}
				set1.add(edgePoint0);
			}
			int startingNode = Integer.parseInt(br.readLine().trim());
			String out = "";
			for (int i = 1; i <= nodes; i++) {
				if (i == startingNode) {
					continue;
				}
				HashSet<Integer> usedNodes = new HashSet<Integer>();
				usedNodes.add(startingNode);
				int edgeCount = countEdges(graph, usedNodes, startingNode, i);
				out += edgeCount != -1 ? edgeCount * 6 : -1;
				if (i != nodes) {
					out += " ";
				}
			}
			System.out.println(out);
		}
	}
}