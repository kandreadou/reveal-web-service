package gr.iti.mklab.reveal;

import com.mongodb.DBObject;
import eu.socialsensor.framework.client.dao.MediaItemDAO;
import eu.socialsensor.framework.client.dao.impl.MediaItemDAOImpl;
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.reveal.mongo.MongoManager;
import gr.iti.mklab.reveal.visual.IndexingManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("reveal/mmapi")
public class HelloController {

    protected MediaItemDAO mediaDao;

    //protected MongoManager mgr = new MongoManager("127.0.0.1", "Linear", "MediaItems");

    public HelloController() {
        String mongoHost = "127.0.0.1";
        try {
            mediaDao = new MediaItemDAOImpl(mongoHost);
        } catch (Exception ex) {
            //ignore
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello everybody!");
        return "hello";
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Greeting greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Greeting(5, "test");
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
    public String collectionsAdd(
            @RequestParam(value = "name", required = true) String name) {
        try {
            IndexingManager.getInstance().createIndex(name);
            return "success";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }

    @RequestMapping(value = "/media/image/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String indexImage(
            @RequestParam(value = "folder", required = false) String folder,
            @RequestParam(value = "name", required = true) String filename) {
        try {
            return String.valueOf(IndexingManager.getInstance().indexImage("/home/kandreadou/Pictures/asdf/",filename));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /*@RequestMapping(value = "/media/test", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DBObject> mediaFromManager() {
        return mgr.search();
    }*/


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