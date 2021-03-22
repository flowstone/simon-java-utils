package me.xueyao;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 树形结构工具
 * @author Simon.Xue
 * @date 2/23/21 9:57 PM
 **/
public class SimonTreeUtil {

    /**
     * 所有子部门的id列表
     */
    private static List<String> childDeptId = new ArrayList<>();

    /**
     * 递归获得 父id下的所有id
     * @param departmentList 所有部门列表
     * @param pid  指定的PID
     * @return
     */
    public static List<String> treeDepartIdList(List<Department> departmentList, String pid) {
        for (Department dept : departmentList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (dept.getPid().equals(pid)) {
                //递归遍历下一级
                treeDepartIdList(departmentList, String.valueOf(dept.getId()));
                childDeptId.add(String.valueOf(dept.getId()));
            }
        }
        return childDeptId;
    }

    /**
     * 递归获得 所有上级部门列表(非树型结构)
     * 不包含自身列表，请注意去重
     * @date 2021-03-22 19:35:25
     * @param departmentAllList 所有部门列表
     * @param departmentChildList 子部门列表(搜索匹配到的部门列表)
     * @param result 最后符合的部门列表
     */
    public static void childParentList(List<Department> departmentAllList,
                                List<Department> departmentChildList,
                                List<Department> result) {

        for (Department child : departmentChildList) {
            if (child.getPid().equals("0")) {
                result.add(child);
                continue;
            }

            Department depart = departmentAllList.stream()
                    .filter(department -> child.getPid().equals(department.getId()))
                    .findFirst()
                    .get();

            result.add(depart);

            if (!depart.getPid().equals("0")) {
                childParentList(departmentAllList, Arrays.asList(depart), result);
            }
        }

    }

    /**
     * 递归所有部门(树型结构)
     * 不建议使用该方法，请使用Hutool工具包中的TreeUtil，简单方便
     * @param code  主部门code
     * @param departmentList 全部部门列表
     * @return 树型结构列表
     */
    public static List<Department> getChild(String code, List<Department> departmentList) {
        ArrayList<Department> childDept = new ArrayList<>();
        for (Department department : departmentList) {
            if (department.getPid().equals(code)) {
                childDept.add(department);
            }
        }

        for (Department child : childDept) {
            child.setDepartmentList(getChild(child.getId(), departmentList));
        }


        if (0 == childDept.size()) {
            return null;
        }
        return childDept;
    }

    @Data
    class Department {
        private String id;
        private String pid;

        List<Department> departmentList;
    }

}
