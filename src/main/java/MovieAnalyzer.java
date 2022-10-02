import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalyzer {
    static List<Movie> moviesList=new ArrayList<>();
    public static class Movie{
        String Poster_Link;
        String Series_Title;
        int Released_Year;
        String Certificate;
        int Runtime;
        String Genre;



        float IMDB_Rating;
        String Overview;
        int Meta_score;
        String Director;
        String Star1;
        String Star2;
        String Star3;
        String Star4;
        int Noofvotes;
        int Gross;
        public String getPoster_Link() {
            return Poster_Link;
        }
        public String getSeries_Title() {
            return Series_Title;
        }

        public int getReleased_Year() {
            return Released_Year;
        }

        public String getCertificate() {
            return Certificate;
        }

        public int getRuntime() {
            return Runtime;
        }

        public String getGenre() {
            return Genre;
        }

        public float getIMDB_Rating() {
            return IMDB_Rating;
        }

        public String getOverview() {
            return Overview;
        }

        public int getMeta_score() {
            return Meta_score;
        }

        public String getDirector() {
            return Director;
        }

        public String getStar1() {
            return Star1;
        }

        public String getStar2() {
            return Star2;
        }

        public String getStar3() {
            return Star3;
        }

        public String getStar4() {
            return Star4;
        }

        public int getNoofvotes() {
            return Noofvotes;
        }

        public int getGross() {
            return Gross;
        }
        public Movie(String Poster_Link,String Series_Title,int Released_Year, String Certificate,int Runtime, String Genre,float IMDB_Rating,String Overview,int Meta_score,String Director,String Star1,String Star2,String Star3,String Star4,int Noofvotes,int Gross){
            this.Poster_Link=Poster_Link;
            this.Series_Title= Series_Title;
           this.Released_Year= Released_Year;
           this. Certificate= Certificate;
           this. Runtime= Runtime;
           this. Genre= Genre;
           this.IMDB_Rating= IMDB_Rating;
           this.Overview= Overview;
           this.Meta_score= Meta_score;
           this.Director= Director;
           this.Star1= Star1;
           this.Star2= Star2;
           this.Star3= Star3;
           this.Star4= Star4;
           this.Noofvotes= Noofvotes;
           this.Gross= Gross;
        }
//        public  String toString(){
//
//            return ;
//
//        }

    }


    public static void main(String[] args) throws IOException {
        MovieAnalyzer mv=new MovieAnalyzer("./resources/imdb_top_500.csv");
        mv.getMovieCountByYear();







    }
    public MovieAnalyzer(String dataset_path)  {

        String line="";
        int num=0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataset_path));
            br.readLine();
            num++;
            while ((line = br.readLine()) != null) {
                String[] b = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> a = transferArrayToList(b);
                if(a.size()==15){
                    //no gross
                    a.add("0");


                }

                     if(a.get(3).contains("min")){
                        //no certificate 3
                        a.set(3,"");

                    } if(!isNumeric(a.get(8))) {
                        //no mate_score 8
                        a.set(8,"0");

                    }
                Movie movie=new Movie(a.get(0).replace("\"",""),a.get(1).replace("\"",""),Integer.parseInt(a.get(2)),a.get(3).replace("\"",""),Integer.parseInt(a.get(4).replace(" min","")),a.get(5).replace("\"",""),Float.parseFloat(a.get(6)),a.get(7).replace("\"",""),Integer.parseInt(a.get(8)),a.get(9).replace("\"",""),a.get(10).replace("\"",""),a.get(11).replace("\"",""),a.get(12).replace("\"",""),a.get(13).replace("\"",""),Integer.parseInt(a.get(14)),Integer.parseInt(a.get(15).replace("\"","").replace(",","")));

                moviesList.add(movie);

                num++;
//                System.out.println(num);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public  Map<Integer, Integer> getMovieCountByYear() {
        Map<Integer, Long> map =moviesList.stream().collect(Collectors.groupingBy(Movie::getReleased_Year,Collectors.counting()));
        Map<Integer,Integer> re=new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        Set<Integer> keySet = map.keySet();


        for (Iterator<Integer> it = keySet.iterator(); it.hasNext(); )
        {
            int key = it.next();
            int value = Math.toIntExact(map.get(key));
            re.put(key,value);

        }
//        System.out.println(re);


        return re;
    }
    public Map<String, Integer> getMovieCountByGenre(){
        return null;
    }
    public Map<List<String>, Integer> getCoStarCount(){
        return null;
    }
    public List<String> getTopMovies(int top_k, String by){
        return null;
    }
    public List<String> getTopStars(int top_k, String by){
        return null;
    }
    public List<String> searchMovies(String genre, float min_rating, int max_runtime){
        return null;
    }
    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }
    private static <E>  List<E> transferArrayToList(E[] array){
        List<E> transferedList = new ArrayList<>();
        Arrays.stream(array).forEach(arr -> transferedList.add(arr));
        return transferedList;
    }

}
