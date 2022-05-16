import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnimeUp extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        RequestDispatcher rd = null;
        String path = req.getServletPath();
        
        if(path.contains("home"))
        {
            // Richiesta dei dati dall'API
            ArrayList<Anime> airing = getTopList("airing");
            ArrayList<Anime> upcoming = getTopList("upcoming");
            ArrayList<Anime> bypopularity = getTopList("bypopularity");

            // Impostazione degli attributi
            req.setAttribute("airing", airing);
            req.setAttribute("upcoming", upcoming);
            req.setAttribute("bypopularity", bypopularity);
            
            // Impostazione della JSP a cui inoltrare in seguito la richiesta
            rd = req.getRequestDispatcher("home.jsp");
            
            // Inoltro della richiesta alla JSP
            rd.forward(req, resp);
        }
        else if(path.contains("search"))
        {
            // Controllo del parametro 'query': verifico che esista e che sia diverso da ""
            if(req.getParameterMap().containsKey("query") && !req.getParameter("query").equals(""))
            {
                String query = req.getParameter("query");

                ArrayList<Anime> results = getSearchResults(query);

                req.setAttribute("query", query);
                req.setAttribute("results", results);

                rd = req.getRequestDispatcher("results.jsp");
                
                // Inoltro della richiesta alla JSP
                rd.forward(req, resp);
            }
            else resp.sendRedirect("home");
        }
    }
    
    private ArrayList<Anime> getTopList(String filter)
    {
        // Inizializzazione Client
        Client c = ClientBuilder.newClient();
        
        // Impostazione dell'endpoint
        WebTarget target = c.target("https://api.jikan.moe/v4/top/anime?filter=" + filter);
        
        // Richiesta dei dati in formato JSON
        String result = target.request(MediaType.APPLICATION_JSON).get(String.class);
        
        return getParsedData(result);
    }
    
    private ArrayList<Anime> getSearchResults(String query)
    {
        // Inizializzazione Client
        Client c = ClientBuilder.newClient();
        
        // Impostazione dell'endpoint
        WebTarget target = c.target("https://api.jikan.moe/v4/anime?q=" + query.replace(" ", "%20") + "&sfw=true");
        
        // Richiesta dei dati in formato JSON
        String result = target.request(MediaType.APPLICATION_JSON).get(String.class);
        
        return getParsedData(result);
    }
    
    private Anime getAnimeObj(JSONObject data)
    {
        String title = data.getString("title");
        String url = data.getString("url");
        String image = data.getJSONObject("images").getJSONObject("webp").getString("image_url");
        
        return new Anime(title, url, image);
    }
    
    private ArrayList<Anime> getParsedData(String data)
    {
        // Parsing del JSON ricevuto
        JSONObject obj = new JSONObject(data);
        JSONArray array = obj.getJSONArray("data");
        
        // Iterazione dell'array
        ArrayList<Anime> list = new ArrayList<>();
        for(int i=0; i<array.length(); i++)
        {
            JSONObject animeData = array.getJSONObject(i);
            // Creazione e inserimento dell'oggetto Anime nella lista
            list.add(getAnimeObj(animeData));
        }
        
        return list;
    }

    public static class Anime
    {
        // Attributi
        private String title;
        private String url;
        private String image;

        public Anime(String title, String url, String image)
        {
            this.title = title;
            this.url = url;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
