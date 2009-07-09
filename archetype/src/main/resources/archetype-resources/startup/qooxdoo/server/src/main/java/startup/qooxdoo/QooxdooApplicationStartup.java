package ${package}.startup.qooxdoo;

import java.util.List;

import net.sf.qooxdoo.rpc.RemoteService;
import net.sf.qooxdoo.rpc.RemoteServiceException;

import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import ${package}.startup.remote.RemoteApplicationStartup;

/**
 * Qooxdoo application startup class.
 */
public class QooxdooApplicationStartup extends RemoteApplicationStartup
    implements RemoteService {

  /**
   * Delegates to start.
   * 
   * @param startupLanguage
   *          the client language.
   * @return the commands to be executed by the client peer on startup.
   * @throws RemoteServiceException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> startQx(String startupLanguage)
      throws RemoteServiceException {
    return super.start(startupLanguage);
  }

  /**
   * Recieves and handle a list of commands.
   * 
   * @param commands
   *          the command list.
   * @return the resulting commands.
   * @throws RemoteServiceException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> handleCommandsQx(List<RemoteCommand> commands)
      throws RemoteServiceException {
    return handleCommands(commands);
  }
}
