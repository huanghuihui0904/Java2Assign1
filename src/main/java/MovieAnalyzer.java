import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalyzer {
  static List<Movie> moviesList = new ArrayList<>();

  public static class Movie {
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

    public Movie(String Poster_Link, String Series_Title, int Released_Year, String Certificate, int Runtime, String Genre, float IMDB_Rating, String Overview, int Meta_score, String Director, String Star1, String Star2, String Star3, String Star4, int Noofvotes, int Gross) {
      this.Poster_Link = Poster_Link;
      this.Series_Title = Series_Title;
      this.Released_Year = Released_Year;
      this.Certificate = Certificate;
      this.Runtime = Runtime;
      this.Genre = Genre;
      this.IMDB_Rating = IMDB_Rating;
      this.Overview = Overview;
      this.Meta_score = Meta_score;
      this.Director = Director;
      this.Star1 = Star1;
      this.Star2 = Star2;
      this.Star3 = Star3;
      this.Star4 = Star4;
      this.Noofvotes = Noofvotes;
      this.Gross = Gross;
    }
//        public  String toString(){
//
//            return ;
//
//        }

  }


  public static void main(String[] args) throws IOException {
    MovieAnalyzer mv = new MovieAnalyzer("./resources/imdb_top_500.csv");
//        mv.getMovieCountByYear();
//mv.getMovieCountByGenre();
//        mv.getCoStarCount();
    mv.getTopMovies(100, "overview");
//        mv.getTopStars(100,"rating");
//        mv.searchMovies("Adventure", 8.0f, 150);

  }

  public MovieAnalyzer(String dataset_path) {

    String line = "";
    int num = 0;
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataset_path), "UTF-8"));
      br.readLine();
      num++;
      while ((line = br.readLine()) != null) {
        String[] b = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        List<String> a = transferArrayToList(b);
        if (a.size() == 15) {
          //no gross
          a.add("0");


        }

        if (a.get(3).contains("min")) {
          //no certificate 3
          a.set(3, "");

        }
        if (!isNumeric(a.get(8))) {
          //no mate_score 8
          a.set(8, "0");

        }
        Movie movie = new Movie(a.get(0).replace("\"", ""), a.get(1).replace("\"", ""), Integer.parseInt(a.get(2)), a.get(3).replace("\"", ""), Integer.parseInt(a.get(4).replace(" min", "")), a.get(5).replace("\"", ""), Float.parseFloat(a.get(6)), clearStartAndEndQuote(a.get(7)), Integer.parseInt(a.get(8)), a.get(9).replace("\"", ""), a.get(10).replace("\"", ""), a.get(11).replace("\"", ""), a.get(12).replace("\"", ""), a.get(13).replace("\"", ""), Integer.parseInt(a.get(14)), Integer.parseInt(a.get(15).replace("\"", "").replace(",", "")));

        moviesList.add(movie);

        num++;
//                System.out.println(num);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public Map<Integer, Integer> getMovieCountByYear() {
    Map<Integer, Long> map = moviesList.stream().collect(Collectors.groupingBy(Movie::getReleased_Year, Collectors.counting()));
    Map<Integer, Integer> re = new TreeMap<>(new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    });
    Set<Integer> keySet = map.keySet();


    for (Iterator<Integer> it = keySet.iterator(); it.hasNext(); ) {
      int key = it.next();
      int value = Math.toIntExact(map.get(key));
      re.put(key, value);

    }
//        System.out.println(re);


    return re;
  }

  public class node implements Comparable {
    public int count;
    public String genre;

    public node(String genre, int count) {
      this.count = count;
      this.genre = genre;

    }

    @Override
    public int compareTo(Object o) {
      node n = (node) o;
      if (this.count == n.count) {
        return this.genre.compareTo(n.genre);
      } else {
        return n.count - count;
      }

    }
  }

  public class Genre {
    String genreName;

    public String getGenreName() {
      return genreName;
    }

    public Genre(String g) {
      this.genreName = g;
    }
  }

  public Map<String, Integer> getMovieCountByGenre() {

/////////////////////////////////////////////////////////////////////////////////////////////////

    List<Genre> temp = new ArrayList<>();
    for (int i = 0; i < moviesList.size(); i++) {
      String[] genres = moviesList.get(i).Genre.replaceAll(" ", "").split(",");
      for (int j = 0; j < genres.length; j++) {
        temp.add(new Genre(genres[j]));
      }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////

//        Map<String, Long> map = moviesList2.stream().collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));
    Map<String, Long> map = temp.stream().collect(Collectors.groupingBy(Genre::getGenreName, Collectors.counting()));

    Set<String> keySet = map.keySet();

    List<node> nodes = new ArrayList<>();
    for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
      String key = it.next();
      int value = Math.toIntExact(map.get(key));

      nodes.add(new node(key, value));

    }
    Collections.sort(nodes);
///////////////////////////////////////////////////
    Map<String, Integer> re = new LinkedHashMap<>();
    for (int i = 0; i < nodes.size(); i++) {
      re.put(nodes.get(i).genre, nodes.get(i).count);
    }

//        System.out.println(re);

    ////////////////////////////////////////////////////////////////
    System.out.println(re);

    return re;
  }

  /////////////////////////////////////////////////////////////////
  public class costar {

    List<String> cos = new ArrayList<>();

    public costar(String a, String b) {
      if (a.compareTo(b) <= 0) {
        cos.add(a);
        cos.add(b);

      } else {
        cos.add(b);
        cos.add(a);
      }

    }

    @Override
    public int hashCode() {
      int result = cos.get(0).hashCode() + cos.get(1).hashCode();
      return result;
    }

    @Override
    public boolean equals(Object o) {
      costar c = (costar) o;
      if (cos.get(0).equals(c.cos.get(0)) && cos.get(1).equals(c.cos.get(1))) {
        return true;
      } else {
        return false;
      }
    }
  }

  public Map<List<String>, Integer> getCoStarCount() {
    Map<costar, Integer> map = new HashMap<>();
    Integer count = 0;
    for (int i = 0; i < moviesList.size(); i++) {
      costar[] c = new costar[6];

      c[0] = new costar(moviesList.get(i).Star1, moviesList.get(i).Star2);
      c[1] = new costar(moviesList.get(i).Star1, moviesList.get(i).Star3);
      c[2] = new costar(moviesList.get(i).Star1, moviesList.get(i).Star4);
      c[3] = new costar(moviesList.get(i).Star2, moviesList.get(i).Star3);
      c[4] = new costar(moviesList.get(i).Star2, moviesList.get(i).Star4);
      c[5] = new costar(moviesList.get(i).Star3, moviesList.get(i).Star4);

      for (int j = 0; j < 6; j++) {
        if ((count = map.get(c[j])) == null) {
          map.put(c[j], 1);
        } else {
          map.put(c[j], 1 + count);
        }
      }
    }

//////////////////////////////////////////////////
    Map<List<String>, Integer> re = new HashMap<>();
//        Map<String, Long> map =moviesList2.stream().collect(Collectors.groupingBy(Movie::getGenre,Collectors.counting()));
    Set<costar> keySet = map.keySet();

//        List<node> nodes=new ArrayList<>();
    for (Iterator<costar> it = keySet.iterator(); it.hasNext(); ) {
      costar key = it.next();
      int value = Math.toIntExact(map.get(key));
      if (value == 2) {
//    System.out.println(key.cos);
      }

      re.put(key.cos, value);
    }
//        System.out.println(re);
    return re;
  }

  //////////////////////////////////////////////////////////////////////////////////////
  public class movie_t implements Comparable {

    String movie_name = "";
    int t = 0;

    public movie_t(String movie_name, int t) {
      this.movie_name = movie_name;
      this.t = t;

    }

//    @Override
//    public int hashCode() {
//        int result = cos.get(0).hashCode() + cos.get(1).hashCode();
//        return result;
//    }

    @Override
    public int compareTo(Object o) {
      movie_t o2 = (movie_t) o;
      if (t == o2.t) {
        return movie_name.compareTo(o2.movie_name);

      } else {
        return o2.t - t;
      }
    }
  }


  public List<String> getTopMovies(int top_k, String by) {


    List<movie_t> mt = new ArrayList<>();

    if (by.equals("runtime")) {
      for (int i = 0; i < moviesList.size(); i++) {
        mt.add(new movie_t(moviesList.get(i).Series_Title, moviesList.get(i).Runtime));
      }
    } else {
      for (int i = 0; i < moviesList.size(); i++) {
        mt.add(new movie_t(moviesList.get(i).Series_Title, moviesList.get(i).Overview.length()));
//               if(moviesList.get(i).Series_Title.equals("Indiana Jones and the Last Crusade")||moviesList.get(i).Series_Title.equals("Ace in the Hole")){
//                   System.out.println(moviesList.get(i).Overview);
//
//               }
      }
    }

    Collections.sort(mt);
    List<String> re = new LinkedList<>();
    for (int i = 0; i < top_k; i++) {
//            System.out.println(mt.get(i).movie_name);

      re.add(mt.get(i).movie_name);
    }
    System.out.println(re);
    return re;
  }

  ////////////////////////////////////////////////////////
  public class star_t implements Comparable {

    String star_name = "";
    double t = 0;

    public star_t(String star_name, double t) {
      this.star_name = star_name;
      this.t = t;

    }

//    @Override
//    public int hashCode() {
//        int result = cos.get(0).hashCode() + cos.get(1).hashCode();
//        return result;
//    }

    @Override
    public int compareTo(Object o) {
      star_t o2 = (star_t) o;
      if (t == o2.t) {
        return star_name.compareTo(o2.star_name);

      } else {
        if (o2.t > t) {
          return 1;
        } else return -1;
      }
    }

    public String getStar_name() {
      return star_name;
    }

    public double getT() {
      return t;
    }
  }

  public List<String> getTopStars(int top_k, String by) {

    List<star_t> st = new ArrayList<>();
    Map<String, Double> map;
    if (by.equals("rating")) {
      for (int i = 0; i < moviesList.size(); i++) {
        if (moviesList.get(i).IMDB_Rating != 0) {
          st.add(new star_t(moviesList.get(i).Star1, moviesList.get(i).IMDB_Rating));
          st.add(new star_t(moviesList.get(i).Star2, moviesList.get(i).IMDB_Rating));
          st.add(new star_t(moviesList.get(i).Star3, moviesList.get(i).IMDB_Rating));
          st.add(new star_t(moviesList.get(i).Star4, moviesList.get(i).IMDB_Rating));
        }

      }
      map = st.stream().collect(Collectors.groupingBy(star_t::getStar_name, Collectors.averagingDouble(star_t::getT)));
    } else {
      for (int i = 0; i < moviesList.size(); i++) {
        if (moviesList.get(i).Gross != 0) {
          st.add(new star_t(moviesList.get(i).Star1, moviesList.get(i).Gross));
          st.add(new star_t(moviesList.get(i).Star2, moviesList.get(i).Gross));
          st.add(new star_t(moviesList.get(i).Star3, moviesList.get(i).Gross));
          st.add(new star_t(moviesList.get(i).Star4, moviesList.get(i).Gross));
        }

      }
      map = st.stream().collect(Collectors.groupingBy(star_t::getStar_name, Collectors.averagingDouble(star_t::getT)));

    }
/////////////////////////////////////////////////////////////
//        Map<String, Long> map = moviesList2.stream().collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));
    Set<String> keySet = map.keySet();

    List<star_t> nodes = new ArrayList<>();
    for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
      String key = it.next();
      double value = map.get(key);

      nodes.add(new star_t(key, value));

    }
    Collections.sort(nodes);

/////////////////////////////////////////////////
//        Collections.sort(st);
    List<String> re = new ArrayList<>();
    for (int i = 0; i < top_k; i++) {
//            System.out.println(nodes.get(i).star_name);

      re.add(nodes.get(i).star_name);
    }
    return re;
  }

  public List<String> searchMovies(String genre, float min_rating, int max_runtime) {

    List<Genre> temp = new ArrayList<>();
    for (int i = 0; i < moviesList.size(); i++) {
      String[] genres = moviesList.get(i).Genre.replaceAll(" ", "").split(",");
      for (int j = 0; j < genres.length; j++) {
        temp.add(new Genre(genres[j]));
      }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////

    List<String> re = new ArrayList<>();
    for (int i = 0; i < moviesList.size(); i++) {
      if (moviesList.get(i).Genre.contains(genre) && moviesList.get(i).IMDB_Rating >= min_rating && moviesList.get(i).Runtime <= max_runtime) {

        re.add(moviesList.get(i).Series_Title);


//                System.out.println(moviesList2.get(i).Series_Title);
      }
    }
    Collections.sort(re);
    for (int i = 0; i < re.size(); i++) {
//            System.out.println(re.get(i));
    }

    return re;
  }

  public static boolean isNumeric(String str) {
    return str != null && str.matches("-?\\d+(\\.\\d+)?");
  }

  private static <E> List<E> transferArrayToList(E[] array) {
    List<E> transferedList = new ArrayList<>();
    Arrays.stream(array).forEach(arr -> transferedList.add(arr));
    return transferedList;
  }

  public static String clearStartAndEndQuote(String str) {
    if (str != null && str.length() >= 2) {
      if (str.indexOf("\"") == 0) str = str.substring(1, str.length());
      if (str.lastIndexOf("\"") == (str.length() - 1)) str = str.substring(0, str.length() - 1);

//            str = str.replaceAll("\"\"","\"");
    }
    return str;
  }


}
