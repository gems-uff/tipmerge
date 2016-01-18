package br.uff.ic.gems.tipmerge.dominoes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

import arch.Cell;
import arch.IMatrix2D;
import arch.Matrix2DFactory;
import domain.Dominoes;

public class DominoesFiles {

    // public static String repository_name = "derby";
    /*
	 * _begin: 2014-01-01 00:00:00 _end: 2014-12-31 00:00:00
     */
    // public static String databaseName = "db/gitdataminer_q0.sqlite";
    // _begin: 2013-01-01 00:00:00
    // _end: 2014-01-31 00:00:00
    // public static String databaseName = "db/gitdataminer.sqlite";
    private static Connection conn = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static final int Developer_Commit = 1;
    public static final int Commit_Method = 2;
    public static final int Package_File = 3;
    public static final int File_Class = 4;
    public static final int Class_Method = 5;
    public static final int Bug_Commit = 6;
    public static final int Commit_File = 7;
    public static final int File_Method = 8;

    public static final int Amount_Tiles = 8;

    public enum Group {
        Month, Day
    }

    private static void openDatabase(String _database)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + _database);
    }

    private static void closeDatabase() throws ClassNotFoundException,
            SQLException {
        conn.close();
        conn = null;
    }

    public static ArrayList<Dominoes> loadMatrices(String _database,
            String _project, String _device, Collection<String> _commitsList,
            Collection<String> _filesList, List<Integer> matrices) throws IOException, SQLException, Exception {

        //Session.startSession();
        openDatabase(_database);

        ArrayList<Dominoes> _dominoesList = new ArrayList<>();

        if (conn != null) {
            Statement smt = conn.createStatement();
            ResultSet rs = null;

            String sql = "SELECT * FROM TMATDESC;";

            rs = smt.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                if (matrices.contains(rs.getInt("mat_id"))) {

                    int _id = rs.getInt("mat_id");
                    String _row_name = rs.getString("row_name");
                    String _col_name = rs.getString("column_name");
                    String _row_ab = rs.getString("row_abbreviate");
                    String _col_ab = rs.getString("column_abbreviate");

                    IMatrix2D _mat = loadMatrixFromDatabase(_id, _row_name,
                            _col_name, _device, _commitsList, _filesList ,_project);

                    if (_mat != null) {
                        Dominoes _dom = new Dominoes(_row_ab, _col_ab, _mat,
                                _device);
                        _dominoesList.add(_dom);
                    }
                }
            }

            rs.close();
            smt.close();
        }
        //Session.closeSection();
        closeDatabase();
        return _dominoesList;
    }

    private static IMatrix2D loadMatrixFromDatabase(int id, String row_name,
            String col_name, String _device, Collection<String> _hashList,
            Collection<String> _filesList, String _project) throws Exception {

        IMatrix2D result = null;

        switch (id) {
            case Developer_Commit:
                result = loadDeveloperCommit(row_name, col_name, _device,
                        _hashList, _project);
                break;

            case Commit_File:
                result = loadCommitFile(row_name, col_name, _device, _hashList, 
                        _filesList, _project);
                break;

            /**
            case Package_File:
                result = loadPackageFile(row_name, col_name, _device, _hashList,
                        _project);
                break;

            case File_Class:
                result = loadFileClass(row_name, col_name, _device, _hashList,
                        _project);
                break;

            case Class_Method:
                result = loadClassMethod(row_name, col_name, _device, _hashList,
                        _project);
                break;

            case Bug_Commit:
                result = loadBugCommit(row_name, col_name, _device, _hashList,
                        _project);
                break;

            case Commit_Method:
                result = loadCommitMethod(row_name, col_name, _device, _hashList,
                        _project);
                break;

            case File_Method:
                result = loadFileMethod(row_name, col_name, _device, _hashList,
                        _project);
            */

        }

        return result;
    }
/**
    private static IMatrix2D loadFileMethod(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading File x Method");

        stopWatch.start();

        sql = "SELECT TF.NewName, TCL.name, TFUNC.name as FuncName FROM TCOMMIT TC, TREPOSITORY TR, TFILE TF "
                + "LEFT JOIN TCLASS AS TCL ON TCL.fileid = TF.id "
                + "LEFT JOIN TFUNCTION AS TFUNC ON TCL.id = TFUNC.classid "
                + "WHERE TF.CommitId = TC.id AND TC.repoid = TR.id AND TR.name = '"
                + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql
                .concat("ORDER BY TC.Date, TF.PackageName, TF.NewName, TCL.Name, FuncName;");
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String fileName = rs.getString("NewName");
            String className = rs.getString("name");
            String funcName = rs.getString("FuncName");
            String composed = className + "$" + funcName;

            if (fileName == null || fileName.equals("null")) {
                continue;
            }

            if (!oldRow.equals(fileName)) {

                if (!descriptor.hasRow(fileName)) {
                    descriptor.AddRowDesc(fileName);
                }

                oldRow = fileName;
            }

            if (funcName == null || funcName.equals("null")) {
                continue;
            }

            if (!oldCol.equals(fileName + "$" + composed)) {

                if (!descriptor.hasCol(fileName + "$" + composed)) {
                    descriptor.AddColDesc(fileName + "$" + composed);
                }

                oldCol = fileName + "$" + composed;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(fileName);
            c.col = descriptor.getColElementIndex(fileName + "$" + composed);
            c.value = 1;

            cells.add(c);
        }

        // System.out.println("File x Method Size: " + descriptor.getNumRows() +
        // " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }
*/

    private static IMatrix2D loadCommitFile(String row, String col,
            String _device, Collection<String> _hashList, Collection<String> _filesList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Commit x File");

        stopWatch.reset();
        stopWatch.start();

        sql = "SELECT TC.HashCode, TF.NewName FROM TCOMMIT TC, TREPOSITORY TR "
                + "LEFT JOIN TFILE AS TF ON TF.CommitId = TC.id "
                + "WHERE TR.name = '" + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql.concat("AND TF.NewName IN ("
                + Arrays.toString(_filesList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql.concat("ORDER BY TF.NewName, TC.Date;");
        //System.out.println("loadCommitFile");
        //System.out.println("\n" + sql + "\n");
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());
        // System.out.println("Commit x File Size: " + descriptor.getNumRows() +
        // " x " + descriptor.getNumCols());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String hashCode = rs.getString("hashcode");
            String newName = rs.getString("NewName");

            if (!oldRow.equals(hashCode)) {

                if (!descriptor.hasRow(hashCode)) {
                    descriptor.AddRowDesc(hashCode);
                }

                oldRow = hashCode;
            }

            if (newName == null || newName.equals("null")) {
                continue;
            }

            if (!oldCol.equals(newName)) {

                if (!descriptor.hasCol(newName)) {
                    descriptor.AddColDesc(newName);
                }

                oldCol = newName;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(hashCode);
            c.col = descriptor.getColElementIndex(newName);
            c.value = 1;
            cells.add(c);
        }

        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);
        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    private static IMatrix2D loadDeveloperCommit(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Developer x Commit");

        stopWatch.reset();
        stopWatch.start();

        // Get all commits
        sql = "SELECT TU.name, TC.HashCode FROM TUser TU, TCOMMIT TC, TREPOSITORY TR "
                + "WHERE TC.userID = TU.id AND TC.RepoId = TR.id AND TR.name = '"
                + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql.concat("ORDER BY TU.name, TC.Date;");
        
        //System.out.println("loadDeveloperCommit");
        //System.out.println("\n" + sql + "\n");
        
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String hashCode = rs.getString("hashcode");
            String name = rs.getString("name");

            if (!oldRow.equals(name)) {

                if (!descriptor.hasRow(name)) {
                    descriptor.AddRowDesc(name);
                }

                oldRow = name;
            }

            if (!oldCol.equals(hashCode)) {

                if (!descriptor.hasCol(hashCode)) {
                    descriptor.AddColDesc(hashCode);
                }

                oldCol = hashCode;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(name);
            c.col = descriptor.getColElementIndex(hashCode);
            c.value = 1;

            cells.add(c);
        }

        // System.out.println("Developer x Commit Size: " +
        // descriptor.getNumRows() + " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    
    /**
    private static IMatrix2D loadPackageFile(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Package x File");

        stopWatch.reset();
        stopWatch.start();

        sql = "SELECT TC.HashCode, TF.NewName, TF.PackageName FROM TCOMMIT TC, TREPOSITORY TR, TFILE TF "
                + "WHERE TF.CommitId = TC.id AND TC.repoid = TR.id AND TR.name = '"
                + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql.concat("ORDER BY TC.Date, TF.PackageName, TF.NewName;");
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String packageName = rs.getString("PackageName");
            String newName = rs.getString("NewName");

            if (packageName == null) {
                continue;
            }

            if (!oldRow.equals(packageName)) {

                if (!descriptor.hasRow(packageName)) {
                    descriptor.AddRowDesc(packageName);
                }

                oldRow = packageName;
            }

            if (newName == null || newName.equals("null")) {
                continue;
            }

            if (!oldCol.equals(newName)) {

                if (!descriptor.hasCol(newName)) {
                    descriptor.AddColDesc(newName);
                }

                oldCol = newName;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(packageName);
            c.col = descriptor.getColElementIndex(newName);
            c.value = 1;

            cells.add(c);
        }

        // System.out.println("Package x File Size: " + descriptor.getNumRows()
        // + " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    
    private static IMatrix2D loadFileClass(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading File x Class");

        stopWatch.start();

        sql = "SELECT TC.HashCode, TF.NewName, TF.PackageName, TCL.name FROM TCOMMIT TC, TREPOSITORY TR, TFILE TF "
                + "LEFT JOIN TCLASS AS TCL ON TCL.fileid = TF.id "
                + "WHERE TF.CommitId = TC.id AND TC.repoid = TR.id AND TR.name = '"
                + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql
                .concat("ORDER BY TC.Date, TF.PackageName, TF.NewName, TCL.Name;");
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String fileName = rs.getString("NewName");
            String className = rs.getString("name");

            if (fileName == null || fileName.equals("null")) {
                continue;
            }

            if (!oldRow.equals(fileName)) {

                if (!descriptor.hasRow(fileName)) {
                    descriptor.AddRowDesc(fileName);
                }

                oldRow = fileName;
            }

            if (className == null || className.equals("null")) {
                continue;
            }

            if (!oldCol.equals(fileName + "$" + className)) {

                if (!descriptor.hasCol(fileName + "$" + className)) {
                    descriptor.AddColDesc(fileName + "$" + className);
                }

                oldCol = fileName + "$" + className;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(fileName);
            c.col = descriptor.getColElementIndex(fileName + "$" + className);
            c.value = 1;

            cells.add(c);
        }

        // System.out.println("File x Class Size: " + descriptor.getNumRows() +
        // " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    private static IMatrix2D loadClassMethod(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Class x Method");

        stopWatch.reset();
        stopWatch.start();

        sql = "SELECT TC.HashCode, TF.NewName, TF.PackageName, TCL.name AS ClassName, "
                + "TM.name as FuncName FROM TCOMMIT TC, TREPOSITORY TR, TFILE TF, TCLASS TCL "
                + "LEFT JOIN TFUNCTION AS TM ON TM.classid = TCL.id "
                + "WHERE TF.CommitId = TC.id AND TCL.fileid = TF.id "
                + "AND TF.NewName NOT LIKE 'null' AND TC.repoid = TR.id AND TR.name = '"
                + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql
                .concat("ORDER BY TC.Date, TF.PackageName, TF.NewName, ClassName, FuncName;");
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String fileName = rs.getString("NewName");
            String className = rs.getString("ClassName");
            String methodName = rs.getString("FuncName");

            if (className == null || className.equals("null")) {
                continue;
            }

            if (!oldRow.equals(fileName + "$" + className)) {

                if (!descriptor.hasRow(fileName + "$" + className)) {
                    descriptor.AddRowDesc(fileName + "$" + className);
                }

                oldRow = fileName + "$" + className;
            }

            if (methodName == null || methodName.equals("null")) {
                continue;
            }

            if (!oldCol.equals(className + "$" + methodName)) {

                if (!descriptor.hasCol(className + "$" + methodName)) {
                    descriptor.AddColDesc(className + "$" + methodName);
                }

                oldCol = className + "$" + methodName;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(fileName + "$" + className);
            c.col = descriptor.getColElementIndex(className + "$" + methodName);
            c.value = 1;

            cells.add(c);
        }

        // System.out.println("Class x Method Size: " + descriptor.getNumRows()
        // + " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    private static IMatrix2D loadCommitMethod(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Commit x Method");

        stopWatch.reset();
        stopWatch.start();

        sql = "SELECT TC.HashCode, TF.NewName, TF.PackageName, TCL.name AS ClassName, "
                + "TM.name as FuncName FROM TCOMMIT TC, TREPOSITORY TR "
                + "LEFT JOIN TFILE AS TF ON TF.commitid = TC.id "
                + "LEFT JOIN TCLASS AS TCL ON TCL.fileid = TF.id "
                + "LEFT JOIN TFUNCTION AS TM ON TM.classid = TCL.id "
                + "WHERE TC.repoid = TR.id AND TR.name = '" + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql
                .concat("ORDER BY TC.date, TF.PackageName, TF.NewName, ClassName, FuncName;");

        rs = smt.executeQuery(sql);
        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String hashCode = rs.getString("hashcode");
            String className = rs.getString("ClassName");
            String newName = rs.getString("FuncName");

            if (!oldRow.equals(hashCode)) {

                if (!descriptor.hasRow(hashCode)) {
                    descriptor.AddRowDesc(hashCode);
                }

                oldRow = hashCode;
            }

            if (newName == null || newName.equals("null")) {
                continue;
            }

            if (!oldCol.equals(className + "$" + newName)) {

                if (!descriptor.hasCol(className + "$" + newName)) {
                    descriptor.AddColDesc(className + "$" + newName);
                }

                oldCol = className + "$" + newName;
            }

            Cell c = new Cell();
            c.row = descriptor.getRowElementIndex(hashCode);
            c.col = descriptor.getColElementIndex(className + "$" + newName);
            c.value = 1;

            cells.add(c);
        }

        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }
    
    */

    private static IMatrix2D loadBugCommit(String row, String col,
            String _device, Collection<String> _hashList, String _project)
            throws Exception {
        String sql;
        arch.MatrixDescriptor descriptor = new arch.MatrixDescriptor(row, col);
        Statement smt = conn.createStatement();
        ResultSet rs;

        StopWatch stopWatch = new StopWatch();
        // System.out.println("*Loading Bug x Commit");

        stopWatch.reset();
        stopWatch.start();

        sql = "SELECT TB.id, TC.hashcode FROM TCOMMIT TC, TREPOSITORY TR "
                + "LEFT JOIN TBUG AS TB ON TB.commitid = TC.hashcode "
                + "WHERE TC.RepoId = TR.id AND TR.name = '" + _project + "' ";

        sql = sql.concat("AND TC.OnlyHash IN ("
                + Arrays.toString(_hashList.toArray()).replace("[", "")
                .replace("]", "") + ") ");

        sql = sql.concat("ORDER BY TC.date, TB.id;");

        /*
		 * este código estava repetido
		 * 
		 * if (_begin != null) sql = sql.concat("AND TC.date >= '" +
		 * sdf.format(_begin) + "' "); if (_end != null) sql =
		 * sql.concat("AND TC.date <= '" + sdf.format(_end) + "' ");
		 * 
		 * sql = sql.concat("ORDER BY TC.date, TB.id;");
         */
        rs = smt.executeQuery(sql);

        stopWatch.stop();
        // System.out.println("**SQL (ms): " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();

        ArrayList<Cell> cells = new ArrayList<Cell>();
        String oldRow = "";
        String oldCol = "";

        while (rs.next()) {
            String id = rs.getString("id");
            String hashCode = rs.getString("hashCode");

            if (id != null && !id.equals("null")) {
                if (!oldRow.equals(id)) {

                    if (!descriptor.hasRow(id)) {
                        descriptor.AddRowDesc(id);
                    }

                    oldRow = id;
                }
            }

            if (!oldCol.equals(hashCode)) {

                if (!descriptor.hasCol(hashCode)) {
                    descriptor.AddColDesc(hashCode);
                }

                oldCol = hashCode;
            }

            if (id != null && !id.equals("null")) {
                Cell c = new Cell();
                c.row = descriptor.getRowElementIndex(id);
                c.col = descriptor.getColElementIndex(hashCode);
                c.value = 1;

                cells.add(c);
            }
        }

        // System.out.println("Bug x Commit Size: " + descriptor.getNumRows() +
        // " x " + descriptor.getNumCols());
        // Build Matrix
        IMatrix2D mat = Matrix2DFactory.getMatrix2D(_device, descriptor);
        mat.setData(cells);

        stopWatch.stop();
        // System.out.println("**Building matriz (ms): " + stopWatch.getTime());
        // System.out.println("**Size: " + descriptor.getNumRows() + " x " +
        // descriptor.getNumCols());

        rs.close();
        smt.close();

        return mat;
    }

    
    
    public static Map<String, Integer> getNumCommits(Group group,
            String _database, Date _begin, Date _end, String _project)
            throws SQLException, ClassNotFoundException {

        if (conn != null) {
            closeDatabase();
        }

        openDatabase(_database);

        String sql = "";
        Statement smt = conn.createStatement();
        ResultSet rs;
        Map<String, Integer> results = new LinkedHashMap<>();

        if (group == Group.Month) {
            sql = "SELECT strftime('%m/%Y', Date) as Period, count(*) as Total FROM TCOMMIT TC, TREPOSITORY TR "
                    + "WHERE TC.RepoId = TR.id AND TR.name = '"
                    + _project
                    + "' ";
        } else if (group == Group.Day) {
            sql = "SELECT strftime('%d/%m/%Y', Date) as Period, count(*) as Total FROM TCOMMIT TC, TREPOSITORY TR "
                    + "WHERE TC.RepoId = TR.id AND TR.name = '"
                    + _project
                    + "' ";
        }

        if (_begin != null) {
            sql = sql.concat("AND TC.date >= '" + sdf.format(_begin) + "' ");
        }
        if (_end != null) {
            sql = sql.concat("AND TC.date <= '" + sdf.format(_end) + "' ");
        }

        if (group == Group.Month) {
            sql = sql.concat("GROUP BY strftime('%m/%Y', date) ");
        } else if (group == Group.Day) {
            sql = sql.concat("GROUP BY strftime('%d/%m/%Y', date) ");
        }

        sql = sql.concat("ORDER BY TC.date;");

        rs = smt.executeQuery(sql);

        while (rs.next()) {
            results.put(rs.getString("Period"), rs.getInt("Total"));
            // System.out.println(rs.getString("Period") + " - " +
            // rs.getInt("Total"));
        }

        rs.close();
        smt.close();

        closeDatabase();

        return results;
    }

    public static Map<String, Integer> getNumBugs(Group group, Date _begin,
            Date _end, String _database, String _project) throws SQLException,
            ClassNotFoundException {

        if (conn != null) {
            closeDatabase();
        }

        openDatabase(_database);

        String sql = "";
        Statement smt = conn.createStatement();
        ResultSet rs;
        Map<String, Integer> results = new LinkedHashMap<>();

        if (group == Group.Month) {
            sql = "SELECT strftime('%m/%Y', Date) as Period, count(*) as Total FROM TCOMMIT TC, TBUG TB, TREPOSITORY TR "
                    + "WHERE TB.commitId = TC.hashcode AND TC.RepoId = TR.id AND TR.name = '"
                    + _project + "' ";
        } else if (group == Group.Day) {
            sql = "SELECT strftime('%d/%m/%Y', Date) as Period, count(*) as Total FROM TCOMMIT TC, TBUG TB, TREPOSITORY TR "
                    + "WHERE TB.commitId = TC.hascode AND TC.RepoId = TR.id AND TR.name = '"
                    + _project + "' ";
        }

        if (_begin != null) {
            sql = sql.concat("AND TC.date >= '" + sdf.format(_begin) + "' ");
        }
        if (_end != null) {
            sql = sql.concat("AND TC.date <= '" + sdf.format(_end) + "' ");
        }

        if (group == Group.Month) {
            sql = sql.concat("GROUP BY strftime('%m/%Y', date) ");
        } else if (group == Group.Day) {
            sql = sql.concat("GROUP BY strftime('%d/%m/%Y', date) ");
        }

        sql = sql.concat("ORDER BY TC.date;");

        rs = smt.executeQuery(sql);

        while (rs.next()) {
            results.put(rs.getString("Period"), rs.getInt("Total"));
            // System.out.println(rs.getString("Period") + " - " +
            // rs.getInt("Total"));
        }

        rs.close();
        smt.close();

        closeDatabase();

        return results;
    }

}
