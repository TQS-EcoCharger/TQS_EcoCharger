import React from 'react';
import styles from '../css/Table.module.css';

export default function Table({ headers, rows }) {
  console.log("Table component rendered with headers:", headers, "and rows:", rows);

  return (
    <div className={styles.tableContainer}>
      <table className={styles.table} id="vehicles-table">
        <thead id="vehicles-table-header">
          <tr className={styles.row}>
            {headers.map((header) => (
              <th key={header} className={styles.headerCell}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody id="vehicles-table-body">
          {rows.length > 0 ? (
            rows.map((row) => {
              const rowKey = row.join('-');
              return (
                <tr key={rowKey} className={styles.row}>
                  {row.map((cell, i) => (
                    <td key={`${rowKey}-${headers[i]}`} className={styles.cell}>{cell}</td>
                  ))}
                </tr>
              );
            })
          ) : (
            <tr className={styles.row}>
              <td colSpan={headers.length} className={styles.emptyRow} id="table-empty-row">
                No data available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
