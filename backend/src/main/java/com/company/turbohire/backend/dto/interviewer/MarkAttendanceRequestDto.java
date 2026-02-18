package com.company.turbohire.backend.dto.interviewer;

import com.company.turbohire.backend.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkAttendanceRequestDto {

    private AttendanceStatus attendanceStatus;

}

