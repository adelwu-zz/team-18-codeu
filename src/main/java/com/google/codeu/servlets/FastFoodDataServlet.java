import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * Returns UFO data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}]
 */
@WebServlet("/fastfood-data")
public class FastFoodDataServlet extends HttpServlet {

  private JsonArray fastFoodArray;

  @Override
  public void init() {
    fastFoodArray = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/fastfood-data.csv"));
    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      double lat = Double.parseDouble(cells[0]);
      double lng = Double.parseDouble(cells[1]);
      String name = cells[2];
      fastFoodArray.add(gson.toJsonTree(new FastFoodRestaurant(lat, lng, name)));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html; charset=UTF-8;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(fastFoodArray.toString());
  }

  // This class could be its own file if we needed it outside this servlet
  private static class FastFoodRestaurant{
    double lat;
    double lng;
    String name;
    private FastFoodRestaurant(double lat, double lng, String name) {
      this.lat = lat;
      this.lng = lng;
      this.name = name;
    }
  }
}
