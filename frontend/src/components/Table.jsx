import React from 'react';
import styles from '../css/Table.module.css';

export default function Table({ headers, rows }) {
  console.log("Table component rendered with headers:", headers, "and rows:", rows);

  return (
    <div className={styles.tableContainer}>
      <table className={styles.table} id="vehicles-table">
        <thead id="vehicles-table-header">
          <tr>
            {headers.map((header) => (
              <th key={header}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody id="vehicles-table-body">
          {rows.length > 0 ? (
            rows.map((row) => {
              const rowKey = row.join('-');
              return (
                <tr key={rowKey}>
                  {row.map((cell, i) => (
                    <td key={`${rowKey}-${headers[i]}`}>{cell}</td>
                  ))}
                </tr>
              );
            })
          ) : (
            <tr>
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
