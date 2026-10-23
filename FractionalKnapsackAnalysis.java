import java.util.*;

class Item {
    String name;
    double weight; 
    double value;  
    double ratio;  

    public Item(String name, double weight, double value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.ratio = value / weight;
    }

    // Copy constructor
    public Item(Item other) {
        this.name = other.name;
        this.weight = other.weight;
        this.value = other.value;
        this.ratio = other.ratio;
    }
}

class Result {
    double totalValue;
    double totalWeight;
    List<String> selectionLog = new ArrayList<>();
}

public class FractionalKnapsackAnalysis {

    public static Result greedyByRatio(List<Item> items, double capacity) {
        List<Item> list = cloneList(items);
        list.sort((a, b) -> Double.compare(b.ratio, a.ratio)); 
        return simulateKnapsack(list, capacity);
    }

    public static Result greedyByValue(List<Item> items, double capacity) {
        List<Item> list = cloneList(items);
        list.sort((a, b) -> Double.compare(b.value, a.value)); 
        return simulateKnapsack(list, capacity);
    }

    public static Result greedyByWeight(List<Item> items, double capacity) {
        List<Item> list = cloneList(items);
        list.sort((a, b) -> Double.compare(a.weight, b.weight));
        return simulateKnapsack(list, capacity);
    }

    private static Result simulateKnapsack(List<Item> sortedItems, double capacity) {
        Result res = new Result();
        double remainingCapacity = capacity;

        for (Item item : sortedItems) {
            if (remainingCapacity <= 0) break;

            if (item.weight <= remainingCapacity) {
                // Take 100%
                res.totalValue += item.value;
                res.totalWeight += item.weight;
                remainingCapacity -= item.weight;
                res.selectionLog.add(String.format("%s: 100.0%% (%.1f MB, Val: %.1f)", item.name, item.weight, item.value));
            } else {
                // Take fraction
                double fraction = remainingCapacity / item.weight;
                double takenValue = item.value * fraction;
                res.totalValue += takenValue;
                res.totalWeight += remainingCapacity;
                res.selectionLog.add(String.format("%s: %.2f%% (%.1f MB, Val: %.1f)", item.name, fraction * 100, remainingCapacity, takenValue));
                remainingCapacity = 0;
            }
        }
        return res;
    }

    private static List<Item> cloneList(List<Item> items) {
        List<Item> copy = new ArrayList<>();
        for (Item item : items) {
            copy.add(new Item(item));
        }
        return copy;
    }

    public static void main(String[] args) {
        List<Item> dataset = new ArrayList<>();
        dataset.add(new Item("Camera Feed A", 20.0, 100.0));
        dataset.add(new Item("Sensor Cluster B", 10.0, 60.0));
        dataset.add(new Item("Satellite Images C", 30.0, 120.0));

        double capacity = 40.0; 

        System.out.println("=========================================================================================");
        System.out.println("              FRACTIONAL KNAPSACK ALGORITHM ANALYSIS (DRONE OPTIMIZATION)                ");
        System.out.println("=========================================================================================");
        System.out.printf("Capacity: %.1f MB | Total Items: %d%n%n", capacity, dataset.size());

        String[] approachNames = {
            "Greedy by Ratio (Optimal)",
            "Greedy by Value (Sub-optimal)",
            "Greedy by Weight (Sub-optimal)"
        };

        for (int i = 0; i < 3; i++) {
            long startTime = System.nanoTime();
            Result res;

            if (i == 0) res = greedyByRatio(dataset, capacity);
            else if (i == 1) res = greedyByValue(dataset, capacity);
            else res = greedyByWeight(dataset, capacity);

            long duration = System.nanoTime() - startTime;

            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("Approach: " + approachNames[i]);
            System.out.println("Execution Time: " + duration + " ns");
            System.out.printf("Storage Used: %.1f / %.1f MB%n", res.totalWeight, capacity);
            System.out.printf("Total Importance Score Achieved: %.2f%n", res.totalValue);
            System.out.println("Selection Breakdown:");
            for (String log : res.selectionLog) {
                System.out.println("  * " + log);
            }
        }
        System.out.println("=========================================================================================");
        System.out.println("Analysis Verdict:");
        System.out.println("1. Ratio Greedy earns 200.00 points (Globally Optimal).");
        System.out.println("2. Value Greedy earns only 170.00 points because high-value items consume disproportionate weight.");
        System.out.println("3. All approaches run in O(n log n) time due to the sorting step.");
        System.out.println("=========================================================================================");
    }
}