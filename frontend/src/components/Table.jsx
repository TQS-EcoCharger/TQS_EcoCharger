import React from 'react';
import styles from '../css/Table.module.css';

export default function Table({headers, rows}) {
  return (
    <div className={styles.tableContainer}>
      <table className={styles.table} id="vehicles-table">
        <thead id="vehicles-table-header">
          <tr>
            {headers.map((header, index) => (
              <th key={index}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody id="vehicles-table-body">
          {rows.length > 0 ? (
            rows.map((row, rowIndex) => (
              <tr key={rowIndex} id={`vehicles-table-row-${rowIndex}`}>
                {row.map((cell, cellIndex) => (
                  <td key={cellIndex}>{cell}</td>
                ))}
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={headers.length} className={styles.emptyRow} id="vehicles-table-empty-row">
                No data available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}