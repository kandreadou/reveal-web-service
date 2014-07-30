package gr.iti.mklab.reveal.web;

import eu.socialsensor.framework.client.dao.MediaItemDAO;
import eu.socialsensor.framework.client.dao.impl.MediaItemDAOImpl;
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.reveal.visual.IndexingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("reveal/mmapi")
public class RevealController {


    protected MediaItemDAO mediaDao;

    private static final Logger logger = LoggerFactory.getLogger(RevealController.class);

    //protected MongoManager mgr = new MongoManager("127.0.0.1", "Linear", "MediaItems");

    public RevealController() {
        String mongoHost = "127.0.0.1";

        try {
            mediaDao = new MediaItemDAOImpl(mongoHost);
        } catch (Exception ex) {
            //ignore
        }
    }


    @RequestMapping(value = "/media", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MediaItem> mediaItems(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        List<MediaItem> list = mediaDao.getLastMediaItems(10);
        return list;
    }

    @RequestMapping(value = "/media/image/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MediaItem mediaItemById(@PathVariable("id") String mediaItemId) {
        MediaItem mi = mediaDao.getMediaItem(mediaItemId);
        return mi;
    }

    @RequestMapping(value = "/media/image/search", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MediaItem> mediaItemsSearch(
            @RequestParam(value = "date", required = false) long date) {
        List<MediaItem> list = mediaDao.getLastMediaItems(50);
        return list;
    }

    @RequestMapping(value = "/collections/add", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public IndexingResult collectionsAdd(
            @RequestParam(value = "name", required = true) String name) {
        try {
            IndexingManager.getInstance().createIndex(name);
            return new IndexingResult();
        } catch (Exception ex) {
            return new IndexingResult(false, ex.toString());
        }
    }

    @RequestMapping(value = "/media/image/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String indexImageFromFile(
            @RequestParam(value = "folder", required = false) String folder,
            @RequestParam(value = "name", required = true) String filename) {
        try {
            return String.valueOf(IndexingManager.getInstance().indexImage("/home/kandreadou/Pictures/asdf/", filename, null));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /*@RequestMapping(value = "/media/{collection}/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String indexImageFromFile(@PathVariable("collection") String collectionName,
                             @RequestParam(value = "folder", required = false) String folder,
                             @RequestParam(value = "name", required = true) String filename) {
        try {
            return String.valueOf(IndexingManager.getInstance().indexImage("/home/kandreadou/Pictures/asdf/", filename, collectionName));
        } catch (Exception e) {
            return e.getMessage();
        }
    }*/

    @RequestMapping(value = "/media/{collection}/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String indexImageFromUrl(@PathVariable("collection") String collectionName,
                                    @RequestParam(value = "imageurl", required = true) String imageurl) {
        try {
            return String.valueOf(IndexingManager.getInstance().indexImage(imageurl, collectionName));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/media/{collection}/statistics", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getStatistics(@PathVariable("collection") String collectionName) {
        try {
            return IndexingManager.getInstance().statistics(collectionName);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/media/post/index", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String indexWithPost(
            @RequestParam(value = "collection", required = true) String collection, @RequestBody IndexingRequest request) {
        try {
            logger.error("test");
            logger.error(request.getCollection());
            for(String url:request.getUrls()){
                logger.error(url);
            }
            return "test";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /*@RequestMapping(value = "/media/test", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DBObject> mediaFromManager() {
        return mgr.search();
    }*/


    /////////////////////////////////////////////////////
    ///////////// TEST STUFF ///////////////////////////
    ////////////////////////////////////////////////////

    /*@ExceptionHandler(IndexingException.class)
    public ModelAndView handleIndexingException(HttpServletRequest request, Exception ex){
        logger.error("Requested URL="+request.getRequestURL());
        logger.error("Exception Raised="+ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("message", ex.getMessage());

        modelAndView.setViewName("/WEB-INF/pages/error.html");
        return modelAndView;
    }*/

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello REVEAL!");
        return "hello";
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Greeting greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Greeting(5, "test");
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public class Greeting {

        private final long id;
        private final String content;

        public Greeting(long id, String content) {
            this.id = id;
            this.content = content;
        }

        public long getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}