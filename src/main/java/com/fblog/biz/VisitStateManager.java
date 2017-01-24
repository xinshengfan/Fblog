package com.fblog.biz;

import com.fblog.core.utils.LogUtils;
import com.fblog.service.PostService;
import com.fblog.service.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文章访问管理器
 */
@Component
public class VisitStateManager {
    private static final String TAG = "VisitStateManager";
    @Autowired
    private PostService postService;
    private ConcurrentMap<String, Integer> visit = new ConcurrentHashMap<>();

    /**
     * 更新后台里的访问量统计，使用定时任务更新
     */
    public void flush() {
        ConcurrentMap<String, Integer> copy = visit;
        visit = new ConcurrentHashMap<String, Integer>();
        if (!copy.isEmpty()) {
            LogUtils.d("TAG", "flush visit stat to database");
        }

        for (Map.Entry<String, Integer> entry : copy.entrySet()) {
            postService.addRCount(entry.getKey(), entry.getValue());
        }
        copy.clear();
        copy = null;
    }

    /**
     * 在每次向前台传输时则记录增加一次
     */
    public void record(String postid) {
        Integer count = visit.get(postid);
    /* 该数据，并发问题忽略 */
        visit.put(postid, count == null ? 1 : count + 1);
    /* 此处更新文章阅读数 */
        PostVO p = postService.loadById(postid);
    /* 此处实际为更改缓存中数据 */
        p.setRcount(p.getRcount() + 1);
    }
}
