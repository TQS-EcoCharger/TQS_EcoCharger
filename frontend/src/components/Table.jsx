import React from 'react';
import styles from '../css/Table.module.css';

export default function Table({ headers, rows }) {
  console.log("Table component rendered with headers:", headers, "and rows:", rows);

  return (
    <div className={styles.tableContainer}>
      <table className={styles.table}>
        <thead>
          <tr>
            {headers.map((header) => (
              <th key={header}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.length > 0 ? (
            rows.map((row) => {
              const rowKey = row.join('-');
              return (
                <tr key={rowKey}>
                  {row.map((cell, i) => (
                    <td key={`${rowKey}-${i}`}>{cell}</td>
                  ))}
                </tr>
              );
            })
          ) : (
            <tr>
              <td colSpan={headers.length} className={styles.emptyRow}>
                No data available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
