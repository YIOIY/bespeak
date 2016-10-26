package cn.mldn.singup.service.back.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import cn.mldn.singup.dao.IDictionaryDAO;
import cn.mldn.singup.dao.INewsDAO;
import cn.mldn.singup.service.back.INewsServiceBack;
import cn.mldn.singup.service.back.abs.AbstractServiceBack;
import cn.mldn.singup.vo.News;

@Service
public class NewsServiceBackImpl extends AbstractServiceBack implements INewsServiceBack {
	@Resource
	private IDictionaryDAO dictionaryDAO;
	@Resource
	private INewsDAO newsDAO;
	
	@Override
	public Map<String, Object> list(String column, String keyWord, int currentPage, int lineSize) {
		Map<String,Object> param = super.handleParams(column, keyWord, currentPage, lineSize) ;
		System.out.println(param);
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("allNews", this.newsDAO.findAllSplit(param)) ;
		result.put("newsCount", this.newsDAO.getAllCount(param)) ;
		return result ; 
	}
	
	@Override
	@RequiresRoles("news")
	@RequiresPermissions("news:add")
	public boolean add(News vo) {
		vo.setPubdate(new Date()); 	// 发布日期设置为今天
		if (this.newsDAO.findByTitle(vo.getTitle()) == null) {
			return this.newsDAO.doCreate(vo) ;
		} 
		return false;
	}
	@Override
	@RequiresRoles("news")
	@RequiresPermissions("news:add")
	public Map<String, Object> addPre() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allNewsType", this.dictionaryDAO.findAllByItem("news")) ;
		return map;
	}
	@Override
	public News getByTitle(String title) {
		return this.newsDAO.findByTitle(title);
	}

}