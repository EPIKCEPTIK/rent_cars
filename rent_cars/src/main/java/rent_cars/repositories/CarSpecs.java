package rent_cars.repositories;

import org.springframework.data.jpa.domain.Specification;
import rent_cars.entities.Car;

public interface CarSpecs {

    static Specification<Car> hasBrand(String brand) {
        if (brand == null || brand.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    static Specification<Car> hasStatus(String status) {
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    static Specification<Car> hasCategory(Long categoryId) {
        if (categoryId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }
}