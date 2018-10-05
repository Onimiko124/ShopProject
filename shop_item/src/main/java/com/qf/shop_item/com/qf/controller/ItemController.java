package com.qf.shop_item.com.qf.controller;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    /**
     * 实现页面静态化
     * 动态的生成页面
     */
    @RequestMapping("/createhtml")
    public String createhtml(@RequestBody Goods goods, HttpServletRequest request){
        System.out.println("进入createhtml方法");
        System.out.println(goods);
        Writer out = null;
        // 创建模版
        try {
            Template template = configuration.getTemplate("item.ftl");
            Map<String,Object> map = new HashMap<>();
            map.put("goods",goods);
            map.put("context",request.getContextPath());
            String path = this.getClass().getResource("/").getPath() + "static/page/" + goods.getId() + ".html";
            System.out.println("classpath" + path);

            out = new FileWriter(path);
            template.process(map,out);
            System.out.println("方法执行完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @RequestMapping("/delByid")
    public void delByid(@RequestBody String id){
        String path = this.getClass().getResource("/").getPath() + "static/page/" + id + ".html";
        System.out.println(path);
        File file = new File(path);
        if(file.exists()){
            file.delete();
        } else {
            System.out.println("删除失败，文件不存在");
        }
    }
}
