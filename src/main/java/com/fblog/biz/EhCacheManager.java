package com.fblog.biz;

import com.fblog.core.dao.constants.OptionConstants;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.service.OptionsService;
import com.fblog.service.PostService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class EhCacheManager {
    @Autowired
    private EhCacheCacheManager manager;
    @Autowired
    private PostService postService;
    @Autowired
    private OptionsService optionsService;

    /**
     * 获取缓存数据分析
     *
     * @return
     */
    public Collection<MapContainer> stats() {
        Collection<String> cns = manager.getCacheNames();
        List<MapContainer> list = new ArrayList<>(cns.size());
        for (String cacheName : cns) {
            MapContainer temp = new MapContainer("name", cacheName);
            Ehcache cache = manager.getCacheManager().getEhcache(cacheName);
            Statistics gateway = cache.getStatistics();
      /* 毫秒 */
            temp.put("averageGetTime", gateway.getAverageGetTime());
            temp.put("hitCount", gateway.getCacheHits());
            temp.put("missCount", gateway.getCacheMisses());
            temp.put("size", gateway.getObjectCount());
            if (cache.isStatisticsEnabled()) {
                temp.put("hitRatio", (float) gateway.getCacheHits() / (gateway.getCacheHits() + gateway.getCacheMisses()));
            }
            list.add(temp);
        }

        return list;
    }

    public int getVisitCount() {
        String vcount = optionsService.getOptionValue(OptionConstants.VISIT_COUNT);
        int postTotalCount = postService.getTotalRCount();
        return Integer.parseInt(vcount)+postTotalCount;
    }

    public void clear(String cacheName) {
        Cache cache = manager.getCacheManager().getCache(cacheName);
        cache.removeAll();
        cache.clearStatistics();
    }

}
