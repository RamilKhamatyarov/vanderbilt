package org.magnum.mobilecloud.video;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.DURATION_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.TITLE_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_DURATION_SEARCH_PATH;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_TITLE_SEARCH_PATH;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VideoController {

    @Autowired
    private VideoRepository repository;

    @RequestMapping(value = VIDEO_SVC_PATH,
                    method = RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {

        Iterable<Video> videos = repository.findAll();

        if (videos == null || !videos.iterator().hasNext()) {
            return new ArrayList<Video>();
        }

        return (Collection<Video>) videos;
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}",
                    method = RequestMethod.GET)
    public @ResponseBody Video getVideoById(@PathVariable("id") long id) {
        return repository.findById(Long.valueOf(id)).orElse(null);
    }

    @RequestMapping(value = VIDEO_SVC_PATH,
                    method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v) {
        repository.save(v);
        return v;
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}/like",
                    method = RequestMethod.POST)
    public Void likeVideo(@PathVariable("id") long id, HttpServletResponse response) {
        Video v = repository.findById(Long.valueOf(id)).orElse(null);

        if (v == null) {
            response.setStatus(404);
            return null;
        }

        if (v.getLikes() == 1) {
            response.setStatus(400);
            return null;
        }

        v.setLikes(v.getLikes() + 1);
        repository.save(v);
        return null;
    }

    @RequestMapping(value = VIDEO_SVC_PATH +  "/{id}/unlike",
                    method = RequestMethod.POST)
    public @ResponseBody Void unlikeVideo(@PathVariable("id") long id) {
        Video v = repository.findById(Long.valueOf(id)).orElse(null);
        v.setLikes(v.getLikes() - 1);
        repository.save(v);
        return null;
    }

    @RequestMapping(value = VIDEO_TITLE_SEARCH_PATH,
                    method = RequestMethod.GET)
    public @ResponseBody Collection<Video> findByTitle(@RequestParam(TITLE_PARAMETER) String title) {
        return repository.findByName(title);
    }

    @RequestMapping(value = VIDEO_DURATION_SEARCH_PATH,
                    method = RequestMethod.GET)
    public @ResponseBody Collection<Video> findByDurationLessThan(@RequestParam(DURATION_PARAMETER) long duration) {
        return repository.findByDurationLessThan(Long.valueOf(duration));
    }
}
