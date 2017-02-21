package com.fblog.biz;

import com.fblog.core.dao.constants.OptionConstants;
import com.fblog.core.utils.LogUtils;
import com.fblog.service.OptionsService;
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
    @Autowired
    private OptionsService optionsService;

    private ConcurrentMap<String, Integer> postVisit = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Integer> visit = new ConcurrentHashMap<>();

    /**
     * 更新后台里的访问量统计，使用定时任务更新
     */
    public void flush() {
        ConcurrentMap<String, Integer> copy = postVisit;
        ConcurrentMap<String, Integer> visitCopy = visit;
        visit = new ConcurrentHashMap<String, Integer>();
        postVisit = new ConcurrentHashMap<String, Integer>();
        if (!copy.isEmpty()) {
            LogUtils.d("TAG", "flush postVisit stat to database");
        }
        for (Map.Entry<String, Integer> entry : copy.entrySet()) {
            postService.addRCount(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : visitCopy.entrySet()) {
            optionsService.updateOptionValue(entry.getKey(), String.valueOf(entry.getValue()));
        }
        copy.clear();
        visitCopy.clear();
        copy = null;
        visitCopy = null;

    }

    /**
     * 在每次向前台传输时则记录增加一次
     */
    public void record(String postid) {
        Integer count = postVisit.get(postid);
    /* 该数据，并发问题忽略 */
        postVisit.put(postid, count == null ? 1 : count + 1);
    /* 此处更新文章阅读数 */
        PostVO p = postService.loadById(postid);
    /* 此处实际为更改缓存中数据 */
        p.setRcount(p.getRcount() + 1);
    }

    /**
     * 在首页刷新，搜索，类别查询等新刷新页面时统计+1
     */
    public void visitCount() {
        Integer count = visit.get(OptionConstants.VISIT_COUNT);
        visit.put(OptionConstants.VISIT_COUNT, count == null ? 1 : count + 1);
    }
}
