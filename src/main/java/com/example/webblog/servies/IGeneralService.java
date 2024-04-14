package com.example.webblog.servies;

import java.text.ParseException;
import java.util.Optional;

public interface IGeneralService<T> {
    Iterable<T> findAll();

    Optional<T> findById(Long id);

    T save(T t);

    Boolean remove(Long id);
}
// là một mô hình chung (generic) cho các dịch vụ cơ bản (CRUD) đối với đối
// tượng kiểu T trong Java. Nó định nghĩa các phương thức cơ bản như findAll
// để lấy tất cả, findById để tìm theo ID, save để lưu trữ hoặc cập nhật,
// và remove để xóa dựa trên ID. Các lớp dịch vụ cụ thể có thể triển khai
// interface này để quản lý các loại đối tượng khác nhau.