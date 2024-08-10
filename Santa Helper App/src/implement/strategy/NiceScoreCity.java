package implement.strategy;

import enums.Cities;
import implement.Child;

import java.util.*;

public class NiceScoreCity implements SortStrategy {
    private LinkedHashMap<Cities, Double> niceScoreCityMap = new LinkedHashMap<>();
    private List<Cities> citiesList = new ArrayList<Cities>(Arrays.asList(Cities.values()));

    /**
     * calculeaza scorul nice al unei liste de obiecte de tip Child
     */
    public Double averagePerCity(final List<Child> list) {
        if (list.size() > 0) {
            Double sum = (double) 0;
            for (Child child : list) {
                sum += child.getAverageScore();
            }
            return sum / list.size();
        }
        return 0.0;
    }

    /**
     * creaza o lista de obiecte Child in functie de orasul dat ca parametru
     */
    public List<Child> createList(final List<Child> list, final Cities city) {
        List<Child> finalList = new ArrayList<>();
        for (Child child : list) {
            if (child.getCity().equals(city)) {
                finalList.add(child);
            }
        }
        return finalList;
    }

    /**
     * creeza HashMap-ul in care includ toate orasele si scorurile nice
     */
    public void createMap(final List<Child> list) {
        for (Cities city : citiesList) {
            List<Child> temporaryList = new ArrayList<>();
            temporaryList = createList(list, city);
            Double niceScoreCity = averagePerCity(temporaryList);
            niceScoreCityMap.put(city, niceScoreCity);
        }
    }

    /**
     * sorteaza descrescator un HashMap in functie de valoarea cheii, si in
     * cazul in care valorile sunt egale se va sorta in functie de prima litera
     * a cheii
     */
    public static LinkedHashMap<Cities, Double> sortByValue(final HashMap<Cities, Double> hm) {
        List<Map.Entry<Cities, Double>> list =
                new LinkedList<Map.Entry<Cities, Double>>(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Cities, Double>>() {
            public int compare(final Map.Entry<Cities, Double> o1,
                               final Map.Entry<Cities, Double> o2) {
                if (Objects.equals(o2.getValue(), o1.getValue())) {
                    return o1.getKey().toString().charAt(0) - o2.getKey().toString().charAt(0);
                }
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        LinkedHashMap<Cities, Double> temp = new LinkedHashMap<Cities, Double>();
        for (Map.Entry<Cities, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * adauga in newlist toate obiectele de tip Child care apartin unui
     * oras si ii sorteaza in functie de id
     */
    public void findChildByCity(final Cities city,
                                final List<Child> newList,
                                final List<Child> searchList) {
        List<Child> temporaryList = new ArrayList<>();
        for (Child child : searchList) {
            if (child.getCity().equals(city)) {
                temporaryList.add(child);
            }
        }
        Collections.sort(temporaryList, new Comparator<Child>() {
            @Override
            public int compare(final Child o1, final Child o2) {
                return o1.getId() - o2.getId();
            }
        });
        newList.addAll(temporaryList);
    }

    /**
     * sorteaza lista de tip Child in functie de scorul nice al oraselor
     */
    @Override
    public List<Child> sort(final List<Child> childList) {
        createMap(childList);
        LinkedHashMap<Cities, Double> niceScoreCityMap1;
        List<Child> newList = new ArrayList<>();
        niceScoreCityMap1 = sortByValue(niceScoreCityMap);
        for (Map.Entry<Cities, Double> ob : niceScoreCityMap1.entrySet()) {
            if (ob.getValue() > 0.0) {
                findChildByCity(ob.getKey(), newList, childList);
            }
        }
        return newList;
    }
}
