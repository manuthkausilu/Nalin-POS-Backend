package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.SaleItemReportDTO;
import com.shashi.possysytembackend.dto.SaleReportDTO;
import com.shashi.possysytembackend.entity.Sale;
import com.shashi.possysytembackend.entity.SaleItem;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;

    @Override
    public List<SaleReportDTO> getDailySaleReport(LocalDate date) {
        return mapSalesToReport(findSalesBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX)));
    }

    @Override
    public List<SaleReportDTO> getMonthlySaleReport(YearMonth month) {
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);
        return mapSalesToReport(findSalesBetween(start, end));
    }

    @Override
    public List<SaleReportDTO> getYearlySaleReport(int year) {
        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
        return mapSalesToReport(findSalesBetween(start, end));
    }

    @Override
    public List<SaleReportDTO> getSaleReportForRange(LocalDateTime start, LocalDateTime end) {
        return mapSalesToReport(findSalesBetween(start, end));
    }

    @Override
    public List<SaleItemReportDTO> getDailySaleItemReport(LocalDate date) {
        return mapSaleItemsToReport(findSalesBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX)));
    }

    @Override
    public List<SaleItemReportDTO> getMonthlySaleItemReport(YearMonth month) {
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);
        return mapSaleItemsToReport(findSalesBetween(start, end));
    }

    @Override
    public List<SaleItemReportDTO> getYearlySaleItemReport(int year) {
        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
        return mapSaleItemsToReport(findSalesBetween(start, end));
    }

    @Override
    public List<SaleItemReportDTO> getSaleItemReportForRange(LocalDateTime start, LocalDateTime end) {
        return mapSaleItemsToReport(findSalesBetween(start, end));
    }

    private List<Sale> findSalesBetween(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findBySaleDateBetween(start, end);
    }

    private List<SaleReportDTO> mapSalesToReport(List<Sale> sales) {
        return sales.stream().map(sale -> {
            int itemCount = sale.getSaleItems() == null ? 0
                    : sale.getSaleItems().stream().mapToInt(SaleItem::getQty).sum();
            return new SaleReportDTO(
                    sale.getSaleId(),
                    sale.getSaleDate(),
                    itemCount,
                    sale.getOriginalTotal(),
                    sale.getItemDiscounts(),
                    sale.getOrderDiscount(),
                    sale.getTotalDiscount(),
                    sale.getTotalAmount()
            );
        }).collect(Collectors.toList());
    }

    private List<SaleItemReportDTO> mapSaleItemsToReport(List<Sale> sales) {
        return sales.stream()
                .filter(sale -> sale.getSaleItems() != null)
                .flatMap(sale -> sale.getSaleItems().stream())
                .collect(Collectors.toMap(
                        item -> item.getProduct().getProductId(),
                        item -> {
                            BigDecimal unitPrice = item.getProduct().getSalePrice() != null
                                    ? item.getProduct().getSalePrice()
                                    : item.getPrice();
                            BigDecimal qty = BigDecimal.valueOf(item.getQty());
                            BigDecimal discountTotal = (item.getDiscount() == null ? BigDecimal.ZERO : item.getDiscount()).multiply(qty);
                            BigDecimal totalPrice = unitPrice.multiply(qty);
                            BigDecimal totalAmount = totalPrice.subtract(discountTotal);
                            return new SaleItemReportDTO(
                                    item.getProduct().getProductId(),
                                    item.getProduct().getProductName(),
                                    item.getQty(),
                                    unitPrice,
                                    totalPrice,
                                    discountTotal,
                                    totalAmount
                            );
                        },
                        (a, b) -> {
                            BigDecimal mergedTotalPrice = a.getTotalPrice().add(b.getTotalPrice());
                            BigDecimal mergedDiscount = a.getDiscount().add(b.getDiscount());
                            return new SaleItemReportDTO(
                                    a.getProductId(),
                                    a.getProductName(),
                                    a.getQty() + b.getQty(),
                                    a.getSalePrice(), // keep unit price
                                    mergedTotalPrice,
                                    mergedDiscount,
                                    mergedTotalPrice.subtract(mergedDiscount)
                            );
                        }
                ))
                .values()
                .stream()
                .sorted((a, b) -> a.getProductId().compareTo(b.getProductId()))
                .collect(Collectors.toList());
    }
}
