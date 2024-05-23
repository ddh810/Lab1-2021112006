import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import javax.swing.*;

public class Main {
    private static Map<String, Map<String, Integer>> graph;
    public static void main(String[] args) {//不会写 全用static
        //读文件
        System.out.println("输入文件名：");
        /*Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();*/
        String str = "1";
        File file = new File(str);
        if (!file.exists()) {
            System.out.println("文件不存在，使用默认文件");
            file = new File("src\\input.txt");
        }
        //转换为小写并保存
        String finalString = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replaceAll("[^A-Za-z]+", " ").toLowerCase().trim();
                stringBuilder.append(line).append(" ");
            }
            finalString = stringBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建图 进入例程
        graph = CreateDirectedGraph(finalString);

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph1 = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for(Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            graph1.addVertex(entry.getKey());
        }
        for(Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            for(Map.Entry<String, Integer> entry1 : entry.getValue().entrySet()) {
                DefaultWeightedEdge edge = graph1.addEdge(entry.getKey(), entry1.getKey());
                graph1.setEdgeWeight(edge,entry1.getValue());
            }
        }

        //创建图形组件
        JGraphXAdapter<String,DefaultWeightedEdge> adapter = new JGraphXAdapter<>(graph1);
        //应用圆形布局
        mxIGraphLayout layout=new mxCircleLayout(adapter);
        layout.execute(adapter.getDefaultParent());

        // 将布局后的图形添加到 mxGraphComponent 中
        mxGraphComponent graphComponent = new mxGraphComponent(adapter);
        graphComponent.zoomAndCenter();
        graphComponent.setAutoExtend(true);
        graphComponent.setBounds(300,300,600,600);
        graphComponent.setGraph(adapter);
        graphComponent.refresh();
        // 设置节点的样式
        Map<String, Object> vertexStyle = new HashMap<>();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "green");
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "white");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "black");
        vertexStyle.put(mxConstants.STYLE_STARTSIZE, 500);
        vertexStyle.put(mxConstants.STYLE_STROKEWIDTH, 2);
        vertexStyle.put(mxConstants.STYLE_FONTSIZE, 13);
        vertexStyle.put(mxConstants.STYLE_ROUNDED, 1);

        graphComponent.getGraph().getStylesheet().setDefaultVertexStyle(vertexStyle);
        graphComponent.setGraph(adapter);
        graphComponent.refresh();
        //设置边样式
        Map<String,Object> style = graphComponent.getGraph().getStylesheet().getDefaultEdgeStyle();
        style.put(mxConstants.STYLE_STROKECOLOR, "green");
        style.put("edgeStyle",mxEdgeStyle.ElbowConnector);
        graphComponent.setGraph(adapter);
        //设置cell样式
//        Map<String,Object> cellStyle = new HashMap<>();
//        cellStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
//        cellStyle.put(mxConstants.STYLE_ALIGN,mxConstants.ALIGN_CENTER);
//        cellStyle.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_TOP);
//        cellStyle.put(mxConstants.STYLE_FILLCOLOR,"#FF9103");
//        cellStyle.put(mxConstants.STYLE_STROKECOLOR,"#E86A00");
//        cellStyle.put(mxConstants.STYLE_FONTCOLOR,"#000000");
//        cellStyle.put(mxConstants.STYLE_ROUNDED, 1);
//        cellStyle.put(mxConstants.STYLE_OPACITY,"80");
//        cellStyle.put(mxConstants.STYLE_STARTSIZE,"16");
//        cellStyle.put(mxConstants.STYLE_FONTSTYLE,1);
//        cellStyle.put(mxConstants.STYLE_GRADIENTCOLOR,"#F8C48B");
//        graphComponent.getGraph().getStylesheet().putCellStyle("newcell",cellStyle);
//        graphComponent.setGraph(adapter);

        //设置总体样式

        graphComponent.refresh();
        //创建JFrame并添加图形组件
        JFrame frame = new JFrame("Directed Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);


        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("选择功能（输入数字）：");
            System.out.println("1.展示有向图\n2.查询桥接词\n3.根据桥接词生成新文本\n" +
                    "4.计算最短路径\n5.随机游走\n6.结束\n");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    ShowDirectedGraph(graph);
                    break;
                case 2:
                    BridgeWords();//fault2
                    break;
                case 3:
                    Scanner scanner = new Scanner(System.in);;
                    System.out.println("输入文本：");
                    String InText = scanner.nextLine();
                    String OutText = generateNewText(InText);
                    System.out.println("Input:"+InText+"\nOutput:"+OutText);
                    break;
                case 4 :
                    Scanner scanner1 = new Scanner(System.in);
                    System.out.println("enter source:");
                    String source = scanner1.nextLine();
                    System.out.println("enter target:");
                    String destination = scanner1.nextLine();
                    if(destination.equals(" ")){
                        Map<String,String>allPaths = allPaths(source);
                        allPaths.forEach((target,path)->{
                            if(path.equals("unreachable")){
                                System.out.println(source+" to "+target+":unreachable");
                            }
                            else{
                                System.out.println(source+" to "+target+":"+path);
                            }
                        });
                    }else{
                        String path = getShortestPath(source,destination);
                        System.out.println(path);
                    }
                    break;
                case 5:
                    randomWalk();
                    break;
                case 6:
                        break;
                default:
                    System.out.println("重新输入\n");
                    break;
            }
        }
    }
    //***Q6
    public static String randomWalk(){//打印fault5
        List<String> list = new ArrayList<>(graph.keySet());
        Random random = new Random();
        String start = list.get(random.nextInt(list.size()));

        List<String> visitedEdges = new ArrayList<>();
        String current = start;
        List<String> paths = new ArrayList<>();
        boolean flag = false;
        System.out.println("start:"+start);

        while(!flag&&graph.containsKey(current)&&!graph.get(current).isEmpty()){
            Map<String,Integer> edges = graph.get(current);
            List<String> nextlist = new ArrayList<>(edges.keySet());
            String next = nextlist.get(random.nextInt(nextlist.size()));
            paths.add(current);

            String edge = current+"->"+next;
            if(visitedEdges.contains(edge)){
                current = next;
                break;
            }//终止条件
            visitedEdges.add(edge);
            current = next;
            //用户停止
            Scanner scanner = new Scanner(System.in);
            System.out.println("current: "+current+" ---enter'D'to end");
            if(scanner.next().equals("D")){
                flag = true;
            }
        }
        paths.add(current);//最后一个补上
        System.out.println("result:"+String.join(",",paths));

        //写文件
        String filepath = "src/randomWalk.txt";
        File file = new File(filepath);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for(String line : paths){
                bufferedWriter.write(line+" ");
            }
            System.out.println("Done");
        }catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }
    //***Q1
    //文件读取创建
    public static Map<String, Map<String, Integer>> CreateDirectedGraph(String finalString) {
        Map<String, Map<String, Integer>> G = new HashMap<>();
        String[] words = finalString.split("\\s+");
        for (int i = 0; i < words.length-1; i++) {//fault1
            String V1 = words[i];
            String V2 = words[i + 1];
            G.putIfAbsent(V1, new HashMap<>());
            Map<String, Integer> E = G.get(V1);
            E.put(V2, E.getOrDefault(V2, 0) + 1);
        }
        return G;
    }
    //***Q2
    //直接打印
    public static void ShowDirectedGraph(Map<String, Map<String, Integer>> G) {
        for (Map.Entry<String, Map<String, Integer>> entry : G.entrySet()) {
            String V1 = entry.getKey();
            Map<String, Integer> E = entry.getValue();
            System.out.println(V1+"->");
            for (Map.Entry<String, Integer> e : E.entrySet()) {
                String V2 = e.getKey();
                Integer V3 = e.getValue();
                System.out.println(V2+":"+V3+";");
            }
            System.out.println();
        }
    }
    //***Q3
    public static void BridgeWords() {
        //判断是否存在
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter word1: ");
        String word1 = scanner.nextLine().toLowerCase();
        System.out.print("Enter word2: ");
        String word2 = scanner.nextLine().toLowerCase();//读
        if(!graph.containsKey(word1)) {
            System.out.println("No "+word1+" found");
            return;
        }
        if(!graph.containsKey(word2)) {
            System.out.println("No "+word2+" found");
            return;
        }

        //保存所有桥接词
        List<String>bridgeWords = queryBridgeWords(word1,word2);
        if(bridgeWords.isEmpty()) {
            System.out.println("No bridge word from "+word1+" to "+word2);
        }else{
            System.out.println("Bridge words from "+word1+" to "+word2+" are:"+String.join(",",bridgeWords));
        }//打印
    }
    //word1的邻接是否存在并且邻接的邻接是不是word2
    public static List<String> queryBridgeWords(String word1,String word2){
        List<String> bridgeWords = new ArrayList<>();
        Map<String,Integer> word1Map = graph.get(word1);
        if(word1Map == null) {
            return bridgeWords;
        }else{
            for(String next : word1Map.keySet()){
                if(graph.containsKey(next)&&graph.get(next).containsKey(word2)) {
                    bridgeWords.add(next);}
            }
        }
        return bridgeWords;
    }
    //***Q4
    public static String generateNewText(String inputText){
        //this.graph=G.getG();//fault3
        StringBuilder result=new StringBuilder();
        String[]Nwords=inputText.split("\\s+");
        Random rand=new Random();
        for(int i=0;i<Nwords.length-1;i++){
            String word1=Nwords[i];
            String word2=Nwords[i+1];
            result.append(word1).append(" ");

            List<String>bridgeWords = queryBridgeWords(word1,word2);//查找 拼接 打印
            if(!bridgeWords.isEmpty()){
                String choice=bridgeWords.get(rand.nextInt(bridgeWords.size()));
                result.append(choice).append(" ");
            }

        }
        result.append(Nwords[Nwords.length-1]).append(" ");//补上最后一个词
        return result.toString();
    }
    //***Q5
    public static String getShortestPath(String source, String destination) {
        Set<String> visited = new HashSet<>();
        Set<String> unvisited = new HashSet<>();
        Map<String,Integer> distance = new HashMap<>();
        Map<String,String> predecessor = new HashMap<>();

        String result;

        distance.put(source, 0);
        unvisited.add(source);
        while (!unvisited.isEmpty()) {
            String current = getCurrent(unvisited,distance);
            unvisited.remove(current);
            for(Map.Entry<String,Integer> entry : graph.get(current).entrySet()) {
                String neighbour = entry.getKey();
                int distanceToNeighbour = entry.getValue();//下一访问节点信息

                if(!visited.contains(neighbour)) {
                    calcShortestPath(neighbour,distanceToNeighbour,current,distance,predecessor);
                    unvisited.add(neighbour);
                }
            }
            visited.add(current);
        }
        return genPath(predecessor,source,destination);
    }
    //获取最近节点
    public static String getCurrent(Set<String> unvisited,Map<String,Integer> distance) {
        String current = "";
        int minDistance = Integer.MAX_VALUE;
        for(String neighbour : unvisited) {
            int distanceToNeighbour = distance.get(neighbour);
            if(distanceToNeighbour < minDistance) {
                minDistance = distanceToNeighbour;
                current = neighbour;
            }
        }
        return current;
    }
    //计算最小值
    public static void calcShortestPath(String neighbour, int distance, String current, Map<String,Integer> distanceMap, Map<String,String> predecessor) {
        int distanceToNeighbour= distanceMap.get(current);
        if(distanceToNeighbour+distance<distanceMap.getOrDefault(neighbour,Integer.MAX_VALUE)){
            distanceMap.put(neighbour,distanceToNeighbour+distance);
            predecessor.put(neighbour,current);//fault4 局部最优到全局最优
        }
    }
    public static String genPath(Map<String,String> predecessor, String source, String destination) {
        String cur=destination;
        LinkedList<String>path = new LinkedList<>();
        if(!predecessor.containsKey(destination)) {
            return "unreachable";
        }
        path.add(cur);
        while(predecessor.containsKey(cur)) {
            cur=predecessor.get(cur);
            path.add(cur+"->");
        }
        Collections.reverse(path);
        return path.toString();
    }

    public static Map<String,String>allPaths(String source) {
        Set<String> visited = new HashSet<>();
        Set<String> unvisited = new HashSet<>();
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> predecessor = new HashMap<>();
        Map<String, String> allpaths = new HashMap<>();

        distance.put(source, 0);
        unvisited.add(source);

        while (!unvisited.isEmpty()) {
            String current = getCurrent(unvisited, distance);
            unvisited.remove(current);
            for (Map.Entry<String, Integer> entry : graph.get(current).entrySet()) {
                String neighbour = entry.getKey();
                int distanceToNeighbour = entry.getValue();
                if (!visited.contains(neighbour)) {
                    calcShortestPath(neighbour, distanceToNeighbour, current, distance, predecessor);
                    unvisited.add(neighbour);
                }
            }
            visited.add(current);
        }

        for (String node : graph.keySet()) {
            allpaths.put(node, genPath(predecessor, source, node));
        }
        return allpaths;
    }
}