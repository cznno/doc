public class CollectTest {
    public static void main(String[] args) {

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            list.add(String.valueOf(Math.random()));
        }
        exec(list, l -> l = new ArrayList());
        exec(list, ArrayList::clear);
    }

    static void exec(ArrayList list, Consumer<ArrayList> consumer) {
        long sum = 0;

        for (int i = 0; i < 20; i++) {
            List l = (List) list.clone();
            long startTime = System.nanoTime();
            consumer.accept(list);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime);
            sum += duration;
        }
        System.out.println(sum / 20);
    }
}