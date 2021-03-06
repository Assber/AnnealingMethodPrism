package AnnealingMethod;

//import static AnnealingMethod.AnnealingMethod.x1ProvidedValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SimulatedAnnealing {

    // Константы:
    public static final double PI = 3.14;  //число Пи
    public static final double G = 6.67 * Math.pow(10, -11);  // Гравитационная постоянная
    public static final double X = 0.0;  //координата по x(горизонт)

    // Рассчитать вероятность принятия
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // Если новое решение лучше, примите его
        if (newEnergy < energy) {
            return 1.0;
        }
        // Если новое решение хуже, вычисляем вероятность принятия
        System.out.println("acceptanceProbability e-n =" + (energy - newEnergy));
        System.out.println("acceptanceProbability =" + Math.exp((energy - newEnergy) * Math.pow(10, 14) / temperature));
        return Math.exp((energy - newEnergy) * Math.pow(10, 18) / temperature);
    }

    public static double roundHalfUp(double num, int signs) {
        return (new java.math.BigDecimal(num).setScale(signs, java.math.BigDecimal.ROUND_HALF_UP)).doubleValue();
    }

    //Заполнение массива по интервалу, шагу и предоставленному значению
    public static List<Double> fillArray(double intervalPercent, double intervalStep, double providedValue) {
        System.out.println("fillArray start");

        List<Double> array = new ArrayList<>();
        double intervalRightValue = providedValue + Math.abs(providedValue) * intervalPercent / 100;//-5,5
        double intervalLeftValue = providedValue - Math.abs(providedValue) * intervalPercent / 100; //-4.5

        if (intervalPercent == 0 || intervalStep == 0) {
            array.add(intervalRightValue);
        } else {
            for (double i = intervalLeftValue; i <= intervalRightValue; i = i + intervalStep) {
                array.add(i);
            }
        }
        System.out.println("fillArray end");
        return array;

    }

    // Получаем случайное значение из массива
    public static double getRandomValue(List<Double> array) {
        Random rand = new Random();
        Double randomValue = array.get(rand.nextInt(array.size()));
        return randomValue;
    }

    // Получаем Массу объекта по формуле
    public static double getMassValueFromFormula(double r, double p) {
        double massValue = 4 / 3 * PI * Math.pow(r, 3) * p;
        return massValue;
    }

    // Получаем w(дельта g) объекта по формуле
    public static double getWValueFromFormula(double p, double x1, double x2, double z1, double z2) {
        // double w = G * massValue * (h / (Math.pow(Math.sqrt(Math.pow(X, 2) + Math.pow(h, 2)), 3)));
        double w = G * p * (x1 * Math.log((Math.pow(x1, 2) + Math.pow(z2, 2)) / (Math.pow(x1, 2) + Math.pow(z1, 2)))
                - x2 * Math.log((Math.pow(x2, 2) + Math.pow(z2, 2)) / (Math.pow(x2, 2) + Math.pow(z1, 2)))
                + 2 * z2 * (Math.atan(x1 / z2) - Math.atan(x2 / z2))
                + 2 * z1 * (Math.atan(x2 / z1) - Math.atan(x1 / z1)));
        return w;
    }

    public static Solution getWDiffRandom(List<Double> x1Arr, List<Double> pArr, List<Double> x2Arr, double wTheoretical, Solution currentSolution, double z1, double z2) {

        double x1 = getRandomValue(x1Arr);
        double p = getRandomValue(pArr);
        double x2 = getRandomValue(x2Arr);

        // Рассчитаем массу объекта:
        //  double m = getMassValueFromFormula(r, p);
        // Рассчитаем w (дельта g)
        double wСalculated = getWValueFromFormula(p, x1, x2, z1, z2);

        // Рассчитаем разницу между теоретической и рассчитанной w
         double wDiff = Math.abs(wTheoretical - wСalculated);
        // double wDiff = Math.pow(wTheoretical - wСalculated, 2);

        currentSolution.setP(p);
        currentSolution.setX1(x1);
        currentSolution.setX2(x2);
        currentSolution.setZ1(z1);
        currentSolution.setZ2(z2);
        currentSolution.setwСalculated(wСalculated);
        currentSolution.setwTheoretical(wTheoretical);
        currentSolution.setwDiff(wDiff);
        return currentSolution;
    }

    public String startCalc(
            double x1ProvidedValue,
            double x1IntervalPercent,
            double x1IntervalStep,
            double x2ProvidedValue,
            double x2IntervalPercent,
            double x2IntervalStep,
            double z1ProvidedValue,
            double z2ProvidedValue,
            double pProvidedValue, double wTheoretical, double pIntervalPercent,
            double pIntervalStep, double temp, double coolingRate) throws Exception {
        System.out.println("Start startCalc: ");

        // Сгенерируем по интервалу и шагу, все возможные вариации r, p, h. Для дальнейшего случайного избятия значения из этого массива:
        // Создадим Массив:
        List<Double> pArr = new ArrayList<>();// Массив p (радиусы)
        List<Double> x1Arr = new ArrayList<>();// Массив p (плотностей)
        List<Double> x2Arr = new ArrayList<>();// Массив h (глубин)

        // Заполнение массивов согласно заданным параметрам:
        //x1 = fillArray(rIntervalPercent, rIntervalStep, rProvidedValue);
        pArr = fillArray(pIntervalPercent, pIntervalStep, pProvidedValue);
        x1Arr = fillArray(x1IntervalPercent, x1IntervalStep, x1ProvidedValue);
        x2Arr = fillArray(x2IntervalPercent, x2IntervalStep, x2ProvidedValue);
        // x2 = fillArray(hIntervalPercent, hIntervalStep, hProvidedValue);

        long x1Arrl = x1Arr.size();
        long pArrl = pArr.size();
        long x2Arrl = x2Arr.size();
        System.out.println("x1Arr.size(): " + x1Arrl);
        System.out.println("pArr.size(): " + pArrl);
        System.out.println("x2Arr.size(): " + x2Arrl);

        long possibleСombinationCount = x1Arrl * pArrl * x2Arrl;
        System.out.println("possibleСombinationCount " + possibleСombinationCount);
        //  начальную температуру temp;
        // Скорость охлаждения coolingRate

        // Инициализация начального решения
        // Выбираем случайное значение r,p,h из заполненного массива:
        Solution currentSolution = new Solution();
        currentSolution = getWDiffRandom(x1Arr, pArr, x2Arr, wTheoretical, currentSolution, z1ProvidedValue, z2ProvidedValue);

        System.out.println("Начальное решение: " + currentSolution.getwDiff());

        // Установить в качестве текущего лучшего
        Solution wDiffBest = currentSolution;
        int iterations = 0;

        //////////////////////////////////////////////////
        //Втыкаем данные на график
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("W - " + new Date());
        //////////////////////////////////////////////////

        // Цикл, пока система не остынет
        while (temp > 1) {
            Solution wDiffCurrentSolution = currentSolution;
            Solution wDiffNewSolution = new Solution();
            getWDiffRandom(x1Arr, pArr, x2Arr, wTheoretical, wDiffNewSolution, z1ProvidedValue, z2ProvidedValue);

            // Получить энергию решений
            double currentEnergy = wDiffCurrentSolution.getwDiff();
            double newEnergy = wDiffNewSolution.getwDiff();
            //System.out.println("newEnergy: " + wDiffNewSolution.getwDiff());
            // Решаем, должны ли мы принять новое решение
            if (acceptanceProbability(currentEnergy, newEnergy, temp) > Math.random()) {
                wDiffCurrentSolution = wDiffNewSolution;
                ///////////////////////////////////////////
                //Втыкаем данные на график
                series1.add(temp, wDiffCurrentSolution.getwDiff());
                //////////////////////////////////////////
            }

            // Отслеживаем лучшее решение, найденное
            if (wDiffCurrentSolution.getwDiff() < wDiffBest.getwDiff()) {
                wDiffBest = wDiffCurrentSolution;

            }

            //Охлождаем систему
            // temp = temp - coolingRate;
            temp *= 1 - coolingRate;
            //System.out.println("temp ="+ temp);

            iterations++;
        }

        String result = "Количество возможных комбинация: " + possibleСombinationCount + "\n"
                + "Решение найдено за: " + iterations + " итераций\n"
                + "Окончательное решение:\n"
                + "wDiff: " + wDiffBest.getwDiff() + "\n"
                + "При p: " + wDiffBest.getP() + "\n"
                + "При x1: " + wDiffBest.getX1() + "\n"
                + "При x2: " + wDiffBest.getX2() + "\n"
                + "При z1: " + wDiffBest.getZ1() + "\n"
                + "При z2: " + wDiffBest.getZ2() + "\n"
                + "wTheoretical: " + wDiffBest.getwTheoretical() + "\n"
                + "wСalculated: " + wDiffBest.getwСalculated() + "\n";
        System.out.println("result: " + result);

        dataset.addSeries(series1);
        ScatterPlot example = new ScatterPlot("График изменение W от температуры", dataset);
        example.setSize(800, 400);
        example.setLocationRelativeTo(null);
        // example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        example.setVisible(true);

        return result;
    }

}
