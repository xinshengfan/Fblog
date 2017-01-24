package com.fblog.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fansion on 19/01/2017.
 * html文本处理
 */
public class JsoupUtils {
    //白名单
    private static final Whitelist whiteList = Whitelist.relaxed();

    static {
        // 增加可信标签到白名单
        whiteList.addTags("embed", "object", "param", "div", "font", "del");
        // 增加可信属性
        whiteList.addAttributes(":all", "style", "class", "id", "name", "on");
        whiteList.addAttributes("object", "width", "height", "classid", "codebase");
        whiteList.addAttributes("param", "name", "value");
        whiteList.addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess",
                "flashvars", "name", "type", "pluginspage");
    }

    /**
     * 对用户输入的内容进行过滤
     */
    public static String filter(String html) {
        return StringUtils.isEmpty(html) ? "" : Jsoup.clean(html, whiteList);
    }

    /**
     * 比较宽松的过滤，但是会过滤掉object，script， span,div等标签，适用于富文本编辑器内容或其他html内容
     */
    public static String simpleFilter(String html) {
        return StringUtils.isEmpty(html) ? "" : Jsoup.clean(html, Whitelist.simpleText());
    }

    /**
     * 去掉所有标签，返回纯文字.适用于textarea，input
     */
    public static String plainText(String html) {
        return Jsoup.parse(html).text();
    }

    public static List<String> getImgesOrLinks(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("img,a");
        List<String> result = new LinkedList<>();
        for (Element ele : elements) {
            boolean isA = "a".equals(ele.nodeName());
            String link = ele.attr(isA ? "href" : "src");
            if (StringUtils.isEmpty(link)) {
                continue;
            }
            if (isA) {
                int questionIndex = link.indexOf("?");
                if (questionIndex > 0) {
                    link = link.substring(0, questionIndex);
                }
                int sepetorIndex = link.lastIndexOf(".");
                String name = link.substring(sepetorIndex + 1).toLowerCase();
                if (FileUtils.isImageExt(name)) {
                    result.add(link);
                }
            } else {
                result.add(link);
            }
        }
        return result;
    }

}
