package com.example.intelligentfarmcore.utils;

import com.example.intelligentfarmcore.pojo.model.SortDTO;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类，用于创建包含排序信息的Pageable对象
 */
public class PageableUtils {

    /**
     * 根据PageReq创建Pageable对象
     * 
     * @param pageReq 分页请求对象
     * @return 包含排序信息的Pageable对象
     */
    public static Pageable createPageable(PageReq pageReq) {
        return createPageable(pageReq.getPageNumber(), pageReq.getPageSize(), pageReq.getSort());
    }

    /**
     * 根据页码、页面大小和排序信息创建Pageable对象
     * 
     * @param pageNumber 页码（从1开始）
     * @param pageSize   页面大小
     * @param sorts      排序信息列表
     * @return 包含排序信息的Pageable对象
     */
    public static Pageable createPageable(Integer pageNumber, Integer pageSize, List<SortDTO> sorts) {
        // 创建基本的Pageable对象（无排序）
        if (sorts == null || sorts.isEmpty()) {
            return PageRequest.of(pageNumber - 1, pageSize);
        }

        // 构建排序对象
        Sort sort = buildSort(sorts);

        // 创建带排序的Pageable对象
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    /**
     * 根据排序DTO列表构建Sort对象
     * 
     * @param sorts 排序信息列表
     * @return Spring Data Sort对象
     */
    private static Sort buildSort(List<SortDTO> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = sorts.stream()
                .map(sortDTO -> {
                    // 将数据库列名转换为实体属性名
                    System.out.println(sortDTO);
                    String property = convertColumnNameToProperty(sortDTO.getField());
                    String direction = sortDTO.getOrder();

                    if ("desc".equalsIgnoreCase(direction)) {
                        return Sort.Order.desc(property);
                    } else {
                        return Sort.Order.asc(property);
                    }
                })
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

    /**
     * 将数据库列名转换为实体属性名
     * 例如：max_capacity -> maxCapacity, room_number -> roomNumber
     * 
     * @param column 列名
     * @return 属性名
     */
    public static String convertColumnNameToProperty(String column) {
        if (column == null) {
            return null;
        }

        // 简单的下划线转驼峰命名
        if (column.contains("_")) {
            String[] parts = column.split("_");
            StringBuilder sb = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i].substring(0, 1).toUpperCase())
                        .append(parts[i].substring(1));
            }
            return sb.toString();
        }

        return column; // 如果没有下划线，则直接返回原始字段名
    }
}