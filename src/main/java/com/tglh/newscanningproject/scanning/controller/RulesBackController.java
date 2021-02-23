package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.service.RulesBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rulesBack")
public class RulesBackController {
    @Autowired
    private RulesBackService rulesBackService;
    List list = new ArrayList<Integer>();
    @RequestMapping("/departMenu")
    @ResponseBody
    public String  departMenu(){
        try{
            // 查询出所有的菜单数据集合
            List<DepartMent> departMenus = rulesBackService.selectTree();
            // 生成菜单树
            List<DepartMent> tree = createTree(0, departMenus);
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", true);
            obj.put("code",200);
            obj.put("data",tree);
            return obj.toJSONString();
        }catch (Exception e){
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", false);
            obj.put("code",407);
            return obj.toJSONString();
        }

    }

    /**
     * 递归生成菜单树,第一次递归
     */
    private List<DepartMent> createTree(int pid, List<DepartMent> depart) {

        //当我们遍历第一级节点的时候
        //第一级节点---紧接着进行下一级---下一级再进行
        if(pid == 0){
            List<DepartMent> treeMenu = new ArrayList<>();
            for (DepartMent departMenu : depart) {
                if (pid == departMenu.getDepartPid()) {
                    if(1 == departMenu.getId()){
                        treeMenu.add(departMenu);
                    }else{
                        treeMenu.add(departMenu);
                        departMenu.set__child(createTree(departMenu.getId(), depart));
                    }

                }
            }
            return treeMenu;
        }else{
                List<DepartMent> treeMenu = new ArrayList<>();
                Optional<DepartMent> departMentOptional = depart.stream().filter(item -> item.getId().equals(pid)).findFirst();
                if(departMentOptional.isPresent()){
                    DepartMent xinfu = new DepartMent();
                    xinfu.setId(departMentOptional.get().getId());
                    xinfu.setDepartName(departMentOptional.get().getDepartName());
                    xinfu.setDepartPid(departMentOptional.get().getDepartPid());
                    treeMenu.add(xinfu);
                    for (DepartMent departMenu : depart) {
                        if (pid == departMenu.getDepartPid()) {
                            treeMenu.add(departMenu);
                            departMenu.set__child(null);
                        }
                    }
                }

            return treeMenu;
        }

    }



}
