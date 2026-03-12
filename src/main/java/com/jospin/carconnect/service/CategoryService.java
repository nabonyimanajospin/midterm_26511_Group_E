package com.jospin.carconnect.service;

import com.jospin.carconnect.dto.CategoryRequest;
import com.jospin.carconnect.exception.BadRequestException;
import com.jospin.carconnect.exception.ResourceNotFoundException;
import com.jospin.carconnect.model.Category;
import com.jospin.carconnect.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Category already exists");
        }
        Category category = Category.builder().name(request.getName()).build();
        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category update(UUID id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    public void delete(UUID id) {
        categoryRepository.delete(getById(id));
    }
}
