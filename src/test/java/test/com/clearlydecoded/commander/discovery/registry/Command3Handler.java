/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.commander.discovery.registry;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class Command3Handler implements CommandHandler<Command3, Command3Response> {

  @Override
  public Command3Response execute(Command3 command) {
    return new Command3Response("Greeting echo: " + command.getEchoGreeting(),
        command.getEchoGreeting(), 34L);
  }

  @Override
  public String getCompatibleCommandType() {
    return Command3.TYPE;
  }

  @Override
  public Class<Command3> getCompatibleCommandClassType() {
    return Command3.class;
  }

  @Override
  public Class<Command3Response> getCompatibleCommandResponseClassType() {
    return Command3Response.class;
  }
}
