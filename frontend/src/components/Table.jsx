import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Table.module.css';

export default function Table({headers, rows}) {
    console.log("Table component rendered with headers:", headers, "and rows:", rows);
  return (
    <div className={styles.tableContainer}>
      <table className={styles.table}>
        <thead>
          <tr>
            {headers.map((header, index) => (
              <th key={index}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.length > 0 ? (
            rows.map((row, rowIndex) => (
              <tr key={rowIndex}>
                {row.map((cell, cellIndex) => (
                  <td key={cellIndex}>{cell}</td>
                ))}
              </tr>
            ))
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