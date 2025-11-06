package com.hcltech.InventoryMgtSystem.specification;
import com.hcltech.InventoryMgtSystem.models.Transaction;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class TransactionFilterTest {
    @Test
    void testByFilterWithNullSearchValue() {
        Specification<Transaction> spec = TransactionFilter.byFilter(null);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Root<Transaction> root = mock(Root.class);

        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.conjunction()).thenReturn(expectedPredicate);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expectedPredicate, result);
    }

    @Test
    void testByMonthAndYear() {
        Specification<Transaction> spec = TransactionFilter.byMonthAndYear(11, 2025);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Root<Transaction> root = mock(Root.class);

        Path<Object> createdAtPath = mock(Path.class);
        when(root.get("createdAt")).thenReturn(createdAtPath);

        Expression<Integer> monthExpr = mock(Expression.class);
        Expression<Integer> yearExpr = mock(Expression.class);

        when(cb.function("month", Integer.class, createdAtPath)).thenReturn(monthExpr);
        when(cb.function("year", Integer.class, createdAtPath)).thenReturn(yearExpr);
        Predicate monthPredicate = mock(Predicate.class);
        Predicate yearPredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);
        when(cb.equal(monthExpr, 11)).thenReturn(monthPredicate);
        when(cb.equal(yearExpr, 2025)).thenReturn(yearPredicate);
        when(cb.and(monthPredicate, yearPredicate)).thenReturn(combinedPredicate);
        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(combinedPredicate, result);
    }
}
