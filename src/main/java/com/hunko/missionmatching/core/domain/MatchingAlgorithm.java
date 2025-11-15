package com.hunko.missionmatching.core.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MatchingAlgorithm {

    private final int originN;
    private final List<List<Edge>> edges;
    private final int[] levels;
    private final int[] iter;
    private final int[] currentAllocation;
    private List<List<Integer>> result;

    public MatchingAlgorithm(int[] capacity) {
        this.originN = capacity.length;
        int n = capacity.length * 2 + 2;
        edges = new ArrayList<>(n);
        iter = new int[n];
        levels = new int[n];
        currentAllocation = new int[n];
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
        }
        initSourceEdges(capacity);
        initSinkEdges(capacity);
        initMiddle(capacity.length);
    }

    public List<List<Integer>> process() {
        if (result != null) {
            return result;
        }
        while (bfs()) {
            Arrays.fill(iter, 0);
            dfs(0, Integer.MAX_VALUE);
        }
        List<List<Integer>> lists = extractMatching();
        this.result = lists;
        return lists;
    }

    private List<List<Integer>> extractMatching() {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 1; i <= originN; i++) {
            result.add(new ArrayList<>());

            // person_i(out)에서 나가는 간선들 확인
            for (Edge edge : edges.get(i)) {
                // person_j(in)으로 가는 간선인지 확인
                if (edge.to >= originN + 1 && edge.to <= 2 * originN) {
                    // 유량이 흘렀다면 i→j 매칭
                    if (edge.flow > 0) {
                        int j = edge.to - originN - 1;
                        result.get(i - 1).add(j);
                    }
                }
            }
        }

        return result;
    }

    private boolean bfs() {
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        Arrays.fill(levels, -1);
        levels[0] = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (Edge edge : edges.get(cur)) {
                if (levels[edge.to] >= 0 || !edge.isActive()) {
                    continue;
                }
                levels[edge.to] = levels[cur] + 1;
                q.add(edge.to);
            }
        }
        return levels[edges.size() - 1] >= 0;
    }

    private int dfs(int node, int flow) {
        if (node == edges.size() - 1) {
            return flow;
        }

        List<EdgeWithIndex> sortedEdges = new ArrayList<>();
        for (int i = iter[node]; i < edges.get(node).size(); i++) {
            Edge e = edges.get(node).get(i);
            if (levels[node] < levels[e.to] && e.cap > e.flow) {
                sortedEdges.add(new EdgeWithIndex(e, i));
            }
        }

        sortedEdges.sort((a, b) -> {
            int allocA = currentAllocation[a.edge.to];
            int allocB = currentAllocation[b.edge.to];
            if (allocA != allocB) {
                return Integer.compare(allocA, allocB);
            }
            // 할당량이 같으면 인덱스 순서 유지
            return Integer.compare(a.index, b.index);
        });

        for (EdgeWithIndex ei : sortedEdges) {
            Edge e = ei.edge;
            iter[node] = ei.index;
            int d = dfs(e.to, Math.min(flow, e.cap - e.flow));
            if (d > 0) {
                e.flow += d;
                edges.get(e.to).get(e.rev).flow -= d;
                currentAllocation[e.to] += d;
                return d;
            }
        }

        iter[node] = edges.get(node).size();
        return 0;
    }

    private void initMiddle(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    continue;
                }
                addEdge(i, n + j, 1);
            }
        }
    }

    private void initSourceEdges(int[] capacity) {
        for (int i = 0; i < capacity.length; i++) {
            addEdge(0, i + 1, capacity[i]);
        }
    }

    private void initSinkEdges(int[] capacity) {
        for (int i = 0; i < capacity.length; i++) {
            addEdge(originN + i + 1, edges.size() - 1, capacity[i]);
        }
    }

    private void addEdge(int from, int to, int cap) {
        this.edges.get(from).add(new Edge(to, cap, this.edges.get(to).size()));
        this.edges.get(to).add(new Edge(from, 0, this.edges.get(from).size() - 1));
    }

    private static class EdgeWithIndex {
        Edge edge;
        int index;

        EdgeWithIndex(Edge edge, int index) {
            this.edge = edge;
            this.index = index;
        }
    }

    private static class Edge {
        private final int to;
        private final int cap;
        private int flow;
        private final int rev;

        public Edge(int to, int cap, int rev) {
            this.to = to;
            this.cap = cap;
            this.flow = 0;
            this.rev = rev;
        }

        public boolean isActive() {
            return flow < cap;
        }
    }
}