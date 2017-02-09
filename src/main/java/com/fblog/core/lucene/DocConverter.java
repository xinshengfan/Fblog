package com.fblog.core.lucene;

import com.fblog.core.dao.entity.MapContainer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import java.util.Arrays;
import java.util.Collection;

/**
 * 节点转换类
 */
public class DocConverter {

  public static MapContainer convert(Document obj){
    MapContainer mc = new MapContainer();
    for(IndexableField field : obj.getFields()){
      mc.put(field.name(), field.stringValue());
    }
    return mc;
  }

  public static MapContainer convert(Document obj, String... filters){
    return convert(obj, Arrays.asList(filters));
  }

  public static MapContainer convert(Document obj, Collection<String> filters){
    MapContainer mc = new MapContainer();
    for(IndexableField field : obj.getFields()){
      if(filters.contains(field.name()))
        continue;
      mc.put(field.name(), field.stringValue());
    }
    return mc;
  }

}
