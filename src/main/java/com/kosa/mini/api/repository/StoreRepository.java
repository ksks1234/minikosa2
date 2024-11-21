package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.search.SearchStoreInterfaceDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query("SELECT new com.kosa.mini.api.dto.store.StoreContentDTO(" +
            "s.storeId, " +
            "s.storeName, " +
            "s.postcode, " +
            "s.roadAddress, " +
            "s.detailAddress, " +
            "s.extraAddress, " +
            "s.storeDescription, " +
            "s.websiteInfo, " +
            "s.openingTime, " +
            "s.closingTime, " +
            "s.contactNumber, " +
            "s.storePhoto, " +
            "s.owner.memberId, " +
            "COALESCE(AVG(r.rating), 0), " +
            "COUNT(r)) " +
            "FROM Store s LEFT JOIN s.reviews r " +
            "WHERE s.storeId = :storeId " +
            "GROUP BY s.storeId, s.storeName, s.postcode, s.roadAddress, " +
            "s.detailAddress, s.extraAddress, s.storeDescription, s.websiteInfo, " +
            "s.openingTime, s.closingTime, s.contactNumber, s.storePhoto, s.owner.memberId")
    StoreContentDTO findStoreWithContent(@Param("storeId") Integer storeId);

    @Query(value = "SELECT s.store_id AS storeId, s.store_name AS storeName,  " +
            " s.store_photo AS storePhoto, s.store_description AS storeDescription,  " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg " +
            "FROM stores s  " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id  " +
            "WHERE s.store_name LIKE CONCAT('%', :query, '%')  " +
            "   OR s.store_description LIKE CONCAT('%', :query, '%') " +
            "GROUP BY s.store_id, s.store_name, s.store_photo, s.store_description  " +
            "ORDER BY :sort ",
            nativeQuery = true)
    List<SearchStoreInterfaceDTO> findByStoreSearch(String query, String sort);

    // 검색어가 있을 때 사용하는 메서드 (최신순)
    @Query(value = "SELECT s.store_id AS storeId, s.store_name AS storeName, " +
            "s.store_photo AS storePhoto, s.store_description AS storeDescription, " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "WHERE s.store_name LIKE CONCAT('%', :query, '%') " +
            "OR s.store_description LIKE CONCAT('%', :query, '%') " +
            "GROUP BY s.store_id, s.store_name, s.store_photo, s.store_description " +
            "ORDER BY s.updated_at DESC",
            nativeQuery = true)
    List<SearchStoreInterfaceDTO> findByStoreSearchOrderByUpdatedAt(@Param("query") String query);

    // 검색어가 없을 때 사용하는 메서드 (최신순)
    @Query(value = "SELECT s.store_id AS storeId, s.store_name AS storeName, " +
            "s.store_photo AS storePhoto, s.store_description AS storeDescription, " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "GROUP BY s.store_id, s.store_name, s.store_photo, s.store_description " +
            "ORDER BY s.updated_at DESC",
            nativeQuery = true)
    List<SearchStoreInterfaceDTO> findAllStoresOrderByUpdatedAt();

    // 검색어가 있을 때 사용하는 메서드 (평점순)
    @Query(value = "SELECT s.store_id AS storeId, s.store_name AS storeName, " +
            "s.store_photo AS storePhoto, s.store_description AS storeDescription, " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "WHERE s.store_name LIKE CONCAT('%', :query, '%') " +
            "OR s.store_description LIKE CONCAT('%', :query, '%') " +
            "GROUP BY s.store_id, s.store_name, s.store_photo, s.store_description " +
            "ORDER BY ratingAvg DESC",
            nativeQuery = true)
    List<SearchStoreInterfaceDTO> findByStoreSearchOrderByRating(@Param("query") String query);

    // 검색어가 없을 때 사용하는 메서드 (평점순)
    @Query(value = "SELECT s.store_id AS storeId, s.store_name AS storeName, " +
            "s.store_photo AS storePhoto, s.store_description AS storeDescription, " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "GROUP BY s.store_id, s.store_name, s.store_photo, s.store_description " +
            "ORDER BY ratingAvg DESC",
            nativeQuery = true)
    List<SearchStoreInterfaceDTO> findAllStoresOrderByRating();


    List<Store> findByStoreNameContainingIgnoreCase(String storeName);

    @Query("SELECT s.storePhoto FROM Store s WHERE s.storeId = :storeId")
    String getStorePhoto(Integer storeId);

}
