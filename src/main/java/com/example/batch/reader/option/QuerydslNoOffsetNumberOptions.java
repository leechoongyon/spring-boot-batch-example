package com.example.batch.reader.option;

import com.example.batch.reader.expression.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@Slf4j
public class QuerydslNoOffsetNumberOptions<T, N extends Number & Comparable<?>> extends QuerydslNoOffsetOptions <T>{

    private N currentId;
    private N lastId;

    private final NumberPath<N> field;

    public QuerydslNoOffsetNumberOptions(@Nonnull NumberPath<N> field,
                                         @Nonnull Expression expression) {
        super(field, expression);
        this.field = field;
    }

    public N getCurrentId() {
        return currentId;
    }

    public N getLastId() {
        return lastId;
    }

    /**
     * 첫번째, 마지막 구해줌.
     * @param query
     * @param page
     */
    @Override
    public void initKeys(JPAQuery<T> query, int page) {
        if(page == 0) {
            initFirstId(query);
            initLastId(query);

            if (log.isDebugEnabled()) {
                log.debug("First Key= "+currentId+", Last Key= "+ lastId);
            }
        }
    }

    /**
     * 첫번째 키를 찾아줌.
     * 오름차순이면 min 값을. 내림차순이면 max 값을.
     * min, max 가 풀스캔하는게 아니기에 속도가 빠름.
     *
     * @param query
     */
    @Override
    protected void initFirstId(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        boolean isGroupByQuery = isGroupByQuery(clone);

        if(isGroupByQuery) {
            currentId = clone
                    .select(field)
                    .orderBy(expression.isAsc()? field.asc() : field.desc())
                    .fetchFirst();
        } else {
            currentId = clone
                    .select(expression.isAsc()? field.min(): field.max())
                    .fetchFirst();
        }

    }

    @Override
    protected void initLastId(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        boolean isGroupByQuery = isGroupByQuery(clone);

        if(isGroupByQuery) {
            lastId = clone
                    .select(field)
                    .orderBy(expression.isAsc()? field.desc() : field.asc())
                    .fetchFirst();
        } else {
            lastId = clone
                    .select(expression.isAsc()? field.max(): field.min())
                    .fetchFirst();
        }
    }


    @Override
    public JPAQuery<T> createQuery(JPAQuery<T> query, int page) {
        if(currentId == null) {
            return query;
        }

        return query
                .where(whereExpression(page))
                .orderBy(orderExpression());
    }

    /**
     * offset 을 계산안해주도록 어디서부터 읽어야 할지 쿼리를 만들어줌.
     * 오름차순이면 마지막 ID 보다 커야 하고. 내림차순이면 마지막 아이디보다 작아야 함.
     * @param page
     * @return
     */
    private BooleanExpression whereExpression(int page) {
        return expression.where(field, page, currentId)
                .and(expression.isAsc()? field.loe(lastId) : field.goe(lastId));
    }

    private OrderSpecifier<N> orderExpression() {
        return expression.order(field);
    }

    /**
     * 마지막 읽은 데이터를 메모리에 저장하는 로직
     * @param item
     */
    @Override
    public void resetCurrentId(T item) {
        //noinspection unchecked
        currentId = (N) getFiledValue(item);

        if (log.isDebugEnabled()) {
            log.debug("Current Select Key= " + currentId);
        }
    }
}
