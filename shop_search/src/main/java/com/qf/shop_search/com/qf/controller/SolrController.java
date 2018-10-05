package com.qf.shop_search.com.qf.controller;

import com.qf.entity.Goods;
import com.qf.entity.SolrPage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/solr")
public class SolrController {

    @Autowired
    private SolrClient solrClient;

    // 通过solr索引库进行查询商品
    /**
     * 添加索引库
     */
    @RequestMapping("/add")
    @ResponseBody
    public boolean Solradd(@RequestBody Goods goods){
        System.out.println("调用了添加索引库");
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id",goods.getId());
        solrInputDocument.addField("gtitle",goods.getTitle());
        solrInputDocument.addField("gimage",goods.getGimage());
        solrInputDocument.addField("ginfo",goods.getGinfo());
        solrInputDocument.addField("gprice",goods.getPrice());

        // 提交到solr索引库进行添加
        try {
            solrClient.add(solrInputDocument);
            solrClient.commit();
            return true;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * solr搜索商品:根据搜索框的关键字对solr索引库进行搜索
     * SolrPage 分页类
     */
    @RequestMapping("/query")
    public String solrQuery(String keyword, SolrPage<Goods> solrPage, Model model) {
        System.out.println("搜索框的关键字-->" + keyword);

        //创建solr查询对象
        SolrQuery solrQuery = new SolrQuery();

        if(keyword==null || keyword.trim().equals("")) {
            // 如果关键字为空，查询所有商品
            solrQuery.setQuery("*:*");
        } else {
            solrQuery.setQuery("gtitle:" + keyword);
        }
        // 设置搜索关键字高亮
        solrQuery.setHighlight(true);   // 开启高亮
        // 设置高亮的前缀和后缀
        solrQuery.setHighlightSimplePre("<font color=\"red\">");
        solrQuery.setHighlightSimplePost("</font>");
        // 设置需要添加高亮的字段
        solrQuery.addHighlightField("gtitle");

        //设置高亮的折叠
//        solrQuery.setHighlightSnippets(3);//摘要分成几部分
//        solrQuery.setHighlightFragsize(7);//每部分的长度
        List<Goods> list = null;
        if(solrPage == null){
            solrPage = new SolrPage<>();
        }
        // 设置分页
        if(solrPage!=null){
            solrQuery.setStart((solrPage.getPage()-1)*solrPage.getPageSize());
            solrQuery.setRows(solrPage.getPageSize());

            QueryResponse query = null;
            // 存放所有搜索到的商品信息
            list = new ArrayList<>();
            try {
                query = solrClient.query(solrQuery);
                // 获得高亮的结果
                Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
                System.out.println("highlighting -->" + highlighting);
                System.out.println("高亮的商品信息");
                for(Map.Entry<String, Map<String, List<String>>> highmap : highlighting.entrySet()){
                    System.out.println("key" + highmap.getKey());
                    System.out.println("value" + highmap.getValue());
                    System.out.println("--------------");
                }

                // 获得普通的查询结果
                SolrDocumentList results = query.getResults();

                // 获取商品的总条数
                long pageSum = results.getNumFound();
                solrPage.setPageSum((int)pageSum);
                // 设置总页数
                solrPage.setPageCount(solrPage.getPageSum() % solrPage.getPageSize() == 0 ?
                        solrPage.getPageSum() / solrPage.getPageSize() :
                        solrPage.getPageSum() / solrPage.getPageSize() + 1);

                for (SolrDocument result : results) {
                    Goods goods = new Goods();
                    goods.setId(Integer.parseInt(result.getFieldValue("id") + ""));
                    goods.setGimage(result.getFieldValue("gimage") + "");
                    goods.setTitle(result.getFieldValue("gtitle") + "");
                    goods.setPrice(Float.parseFloat(result.getFieldValue("gprice") +""));

                    // 高亮内容的处理
                    if(highlighting.containsKey(goods.getId() + "")){
                        // 如果包含了id，说明当前商品中有高亮的信息
                        List<String> gtitles = highlighting.get(goods.getId() + "").get("gtitle");
                        if(gtitles != null){
                            goods.setTitle(gtitles.get(0));
                        }
                    }
                    list.add(goods);
                    System.out.println(goods.getTitle());
                }
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            // 将查询的结果放入分页对象
            solrPage.setDatas(list);
            model.addAttribute("page",solrPage);
            model.addAttribute("keyword",keyword);
        return "searchlist";
    }

    @RequestMapping("/delByid")
    @ResponseBody
    public Boolean delByid(@RequestBody String id){
        // 从索引库中把后台删除的商品对应id的索引删除
        if(id != null && !"".equals(id)){
            try {
                System.out.println("接收到id-->" + id);
                solrClient.deleteById(id);
                solrClient.commit();
                return true;
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
