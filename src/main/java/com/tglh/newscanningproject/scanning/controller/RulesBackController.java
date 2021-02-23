package com.tglh.newscanningproject.scanning.controller;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.service.RulesBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public List<DepartMent> departMenu(){
        // 查询出所有的菜单数据集合
        List<DepartMent> departMenus = rulesBackService.selectTree();
        // 生成菜单树
        List<DepartMent> tree = createTree(0, departMenus);
        return tree;
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
                    treeMenu.add(departMenu);
                    departMenu.setChildren(createTree(departMenu.getId(), depart));
                }
            }
            return treeMenu;
        }else{
                List<DepartMent> treeMenu = new ArrayList<>();
                Optional<DepartMent> departMentOptional = depart.stream().filter(item -> item.getId().equals(pid)).findFirst();
                if(departMentOptional.isPresent()){
                    treeMenu.add(departMentOptional.get());
                    for (DepartMent departMenu : depart) {
                        if (pid == departMenu.getDepartPid()) {
                            treeMenu.add(departMenu);
                            departMenu.setChildren(null);
                        }
                    }
                }

            return treeMenu;
        }

    }



}
