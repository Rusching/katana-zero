package edu.uchicago.gerber._03objects;

class Country {
    private final String name;
    private final int population;
    private final double area;
    private final double  populationDensity;
    private static double largestPopulation = 0;
    private static String largestPopulationCountry = null;
    private static double largestArea = 0;
    private static String largestAreaCountry = null;
    private static double largestPopulationDensity = 0;
    private static String largestPopulationDensityCountry = null;
    public Country(String name, int population, double area) {
        if (population <= 0) {throw new IllegalArgumentException("Population must be greater than 0.");}
        if (area <= 0) {throw new IllegalArgumentException("Area must be greater than 0.");}
        this.name = name;
        this.population = population;
        this.area = area;
        this.populationDensity = this.population / this.area;

        if (this.population > largestPopulation) {
            largestPopulation = this.population;
            largestPopulationCountry = this.name;
        }
        if (this.area > largestArea) {
            largestArea = this.area;
            largestAreaCountry = this.name;
        }
        if (this.populationDensity > largestPopulationDensity) {
            largestPopulationDensity = this.populationDensity;
            largestPopulationDensityCountry = this.name;
        }
    }

    public static String getLargestAreaCountry() {
        return largestAreaCountry;
    }

    public static double getLargestArea() {
        return largestArea;
    }

    public static String getLargestPopulationCountry() {
        return largestPopulationCountry;
    }

    public static double getLargestPopulation() {
        return largestPopulation;
    }

    public static String getLargestPopulationDensityCountry() {
        return largestPopulationDensityCountry;
    }

    public static double getLargestPopulationDensity() {
        return largestPopulationDensity;
    }
}
public class P8_14 {
    public static void main(String[] args) {
        Country c1 = new Country("Lirendor", 15245000, 500321);
        Country c2 = new Country("Arquelia", 4863210, 230405);
        Country c3 = new Country("Marnolia", 21043572, 180132);
        Country c4 = new Country("Venstria", 7894215, 340675);
        Country c5 = new Country("Jelvanya", 95328, 420);
        Country c6 = new Country("Quoritania", 12231482, 781);
        Country c7 = new Country("Derlandia", 3548912, 215675);

        System.out.println(String.format("The country with largest area %.2f km2 is %s", Country.getLargestArea(), Country.getLargestAreaCountry()));
        System.out.println(String.format("The country with largest population %.2f people is %s", Country.getLargestPopulation(), Country.getLargestPopulationCountry()));
        System.out.println(String.format("The country with largest population density %.2f people per km2 is %s", Country.getLargestPopulationDensity(), Country.getLargestPopulationDensityCountry()));
    }
}
