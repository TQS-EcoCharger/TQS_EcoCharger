import React, { useEffect, useState } from "react";
import moment from "moment";
import styles from "../css/ChargingPointReservations.module.css";

export default function ChargingPointReservations({ reservations, chargingPointId }) {
  const [modalOpen, setModalOpen] = useState(false);
  const [curReservations, setCurReservations] = useState(reservations || []);
  const [curChargingPointId, setCurChargingPointId] = useState(chargingPointId || "");

  useEffect(() => {
      setCurReservations(reservations);
      setCurChargingPointId(chargingPointId);
  }, [reservations, chargingPointId]);

  const filtered = curReservations.filter(
    (r) => r.chargingPoint.id === chargingPointId
  );

  const startDate = moment().startOf("day");
  const days = Array.from({ length: 5 }, (_, i) => startDate.clone().add(i, "days"));
  const hours = Array.from({ length: 24 }, (_, i) => i);

  const isReserved = (day, hour) => {
    const start = day.clone().hour(hour);
    const end = start.clone().add(1, "hour");
    return filtered.some((r) => {
      const resStart = moment(r.startTime);
      const resEnd = moment(r.endTime);
      return resStart.isBefore(end) && resEnd.isAfter(start);
    });
  };

  return (
    <div id="charging-point-reservations-container">
      <button
        id="open-schedule-modal-btn"
        className={styles.openModalBtn}
        onClick={() => setModalOpen(true)}
      >
        Schedule
      </button>

      {modalOpen && (
        <div
          id="schedule-modal-overlay"
          className={styles.modalOverlay}
          onClick={() => setModalOpen(false)}
        >
          <div
            id="schedule-modal-content"
            className={styles.modalContent}
            onClick={e => e.stopPropagation()}
          >
            <button
              id="close-schedule-modal-btn"
              className={styles.closeModalBtn}
              onClick={() => setModalOpen(false)}
              aria-label="Close modal"
            >
              &times;
            </button>

            <h3 id="schedule-modal-title" className={styles.title}>
              Schedule
            </h3>

            <div id="schedule-table-wrapper" className={styles.tableWrapper}>
              <table id="schedule-table" className={styles.table}>
                <thead>
                  <tr id="schedule-table-header-row">
                    <th id="header-empty-cell" className={styles.headerCell}></th>
                    {days.map((day) => (
                      <th
                        key={day.format("YYYY-MM-DD")}
                        id={`header-day-${day.format("YYYY-MM-DD")}`}
                        className={styles.headerCell}
                      >
                        {day.format("ddd, MMM D")}
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody id="schedule-table-body">
                  {hours.map((hour) => (
                    <tr key={hour} id={`hour-row-${hour}`}>
                      <td
                        id={`hour-label-${hour}`}
                        className={`${styles.cell} ${styles.hourLabel}`}
                      >
                        {`${hour}:00`}
                      </td>
                      {days.map((day) => {
                        const reserved = isReserved(day, hour);
                        return (
                          <td
                            key={`${day.format("YYYY-MM-DD")}-${hour}`}
                            id={`cell-${day.format("YYYY-MM-DD")}-${hour}`}
                            className={`${styles.cell} ${reserved ? styles.reserved : styles.available}`}
                          />
                        );
                      })}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
