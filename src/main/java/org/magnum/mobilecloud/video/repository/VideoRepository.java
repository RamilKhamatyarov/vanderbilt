package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    /**
     *
     * @param name
     * @return
     */
    Collection<Video> findByName(String name);

    /**
     *
     * @param duration
     * @return
     */
    Collection<Video> findByDurationLessThan(Long duration);
}
